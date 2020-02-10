package com.softsquared.android.corona.src.main.community;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.community.interfaces.CommunityView;
import com.softsquared.android.corona.src.main.community.model.Post;
import com.softsquared.android.corona.src.main.info.CaringInfo;
import com.softsquared.android.corona.src.main.info.NewsAdapter;

import java.util.ArrayList;

import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;


public class CommunityFragment extends BaseFragment implements CommunityView, SwipeRefreshLayout.OnRefreshListener {

    ViewPager mViewPager;
    NewsAdapter newsAdapter;
    TabLayout mTabLayout;
    Button mBtnCancel, mBtnWrite;
    ImageView mImageViewMask, mImageViewHand;
    LinearLayout mLinearHeader, mLinearContent;
    LikeCheckDialog likeCheckDialog;
    EditText mEditTextPostWriteTitle, mEditTextPostWriteContent;


    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();
    boolean mIsExpanded = false;

    ArrayList<Post> mHotPosts = new ArrayList<>();
    HotPostAdapter mHotPostAdapter;
    RecyclerView mRecyclerView;

    private int mPage = 0;
    private static int mSize = 20;

    boolean mNoMoreItem = false;
    boolean mLoadLock = false;

    ArrayList<Post> mNewPosts = new ArrayList<>();
    NewPostAdapter mNewPostAdapter;
    RecyclerView mNewRv;

    String mFcmToken;
    private SwipeRefreshLayout swipeRefreshLayout;

    private final long FINISH_INTERVAL_TIME = 1000;
    private long clickPressedTime = 0;
    private long clickPressedTime2 = 0;

    boolean isFirst = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_community, container, false);
        setComponentView(view);
        if (isFirst){
            getHotPost();
            getNewPost(mPage);
            isFirst = false;
        }
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void setComponentView(View v) {
        SharedPreferences spf = sSharedPreferences;
        mFcmToken = spf.getString("fcm", null);
        mTabLayout = v.findViewById(R.id.home_tab_indicator);
        mLinearHeader = v.findViewById(R.id.fragment_community_linear_header);
        mLinearContent = v.findViewById(R.id.fragment_community_linear_content);
        mBtnCancel = v.findViewById(R.id.dialog_infect_select_btn_cancel);
        mIsExpanded = false;
        mArrayListCaringInfos.clear();
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_invite, "", 1));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_tip, "", 2));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.img_mask, "KF94마스크", 3));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.img_hand, "손세정제", 3));

        newsAdapter = new NewsAdapter(getChildFragmentManager(), mArrayListCaringInfos);

        mViewPager = v.findViewById(R.id.fragment_info_vp);
//       뷰페이저 미리보기 설정//
        mViewPager.setClipToPadding(false);

        swipeRefreshLayout = v.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        mViewPager.setAdapter(newsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

//        mImageViewMask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "KF94마스크");
//                startActivity(intent3);
//            }
//        });
//        mImageViewHand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "손세정제");
//                startActivity(intent3);
//            }
//        });
        mLinearHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mIsExpanded) {
                    showWrite();
                    mIsExpanded = true;
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideWrite();
                mIsExpanded = false;
            }
        });

        mEditTextPostWriteTitle = v.findViewById(R.id.post_write_title);
        mEditTextPostWriteContent = v.findViewById(R.id.post_write_content);

        mBtnWrite = v.findViewById(R.id.dialog_infect_select_btn_post);
        mBtnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextPostWriteTitle.getText().toString().length() < 2) {
                    showCustomToast("제목을 2글자 이상 입력해주세요.");
                } else if (mEditTextPostWriteContent.getText().toString().length() < 5) {
                    showCustomToast("내용은 5글자 이상 입력해주세요.");
                } else {
                    WriteCheckDialog writeCheckDialog = new WriteCheckDialog(getContext(), new WriteCheckDialog.CustomLIstener() {
                        @Override
                        public void okClick() {
                            postWritePost(mFcmToken, mEditTextPostWriteTitle.getText().toString(), mEditTextPostWriteContent.getText().toString());
                        }

                        @Override
                        public void cancelClick() {

                        }
                    });
                    writeCheckDialog.show();
                }
            }
        });

        mRecyclerView = v.findViewById(R.id.hot_post_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });
        mHotPostAdapter = new HotPostAdapter(getContext(), mHotPosts, new HotPostAdapter.HotPostAdapterListener() {
            @Override
            public void Click(int postNo, int position) {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - clickPressedTime;
                if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                }
                else{
                    clickPressedTime = tempTime;
                    Intent intent = new Intent(getContext(), PostDetailActivity.class);
                    intent.putExtra("postNo", postNo);
                    startActivity(intent);
                }
            }

            @Override
            public void likeClick(final int postNo, final int position) {
                likeCheckDialog = new LikeCheckDialog(getContext(), new LikeCheckDialog.CustomLIstener() {
                    @Override
                    public void okClick() {
                        postLike(postNo, 1, position);
                    }

                    @Override
                    public void cancelClick() {

                    }
                });
                likeCheckDialog.show();
            }
        });
        mRecyclerView.setAdapter(mHotPostAdapter);

        mNewRv = v.findViewById(R.id.new_post_rv);
        mNewRv.setLayoutManager(new LinearLayoutManager(getContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        });

        mNewPostAdapter = new NewPostAdapter(getContext(), mNewPosts, new NewPostAdapter.NewPostAdapterListener() {
            @Override
            public void click(int postNo, int position) {
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - clickPressedTime2;
                if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                }
                else{
                    clickPressedTime2 = tempTime;
                    Intent intent = new Intent(getContext(), PostDetailActivity.class);
                    intent.putExtra("postNo", postNo);
                    startActivity(intent);
                }
            }

            @Override
            public void likeClick(final int postNo, final int position) {
                likeCheckDialog = new LikeCheckDialog(getContext(), new LikeCheckDialog.CustomLIstener() {
                    @Override
                    public void okClick() {
                        postLike(postNo, 2, position);
                    }

                    @Override
                    public void cancelClick() {

                    }
                });
                likeCheckDialog.show();
            }
        });

        mNewRv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int lastVisiblePosition = ((LinearLayoutManager) mNewRv.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = mNewRv.getAdapter().getItemCount();
//                Log.d("스크롤", lastVisiblePosition + "//" + itemTotalCount * 0.7);
                if (lastVisiblePosition > itemTotalCount * 0.7) {
                    if (!mLoadLock) {
                        mLoadLock = true;
                        if (!mNoMoreItem) {
                            getNewPost(mPage);
                        }
                    }
                }
            }
        });

        mNewRv.setAdapter(mNewPostAdapter);

        mEditTextPostWriteContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    // 터치가 눌렸을때 터치 이벤트를 활성화한다.
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    // 터치가 끝났을때 터치 이벤트를 비활성화한다 [원상복구]
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }

    private void showWrite() {
        float dp = getContext().getResources().getDisplayMetrics().density;
        ValueAnimator anim2 = ValueAnimator.ofInt(0, (int) (300f * dp));
        anim2.setDuration(500); // duration 5 seconds
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
        mIsExpanded = true;
    }


    private void hideWrite() {
        ValueAnimator anim2 = ValueAnimator.ofInt(800, 0);
        anim2.setDuration(500); // duration 5 seconds
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
    }

    void getHotPost() {
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.getHotPost();
    }

    void postLike(int postNo, int mode, int position) {
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.postLike(postNo, mFcmToken, mode, position);
    }

    void getNewPost(int mPage) {
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.getNewPost(mPage);
    }

    void postWritePost(String fcmToken, String title, String content) {
        showProgressDialog(getActivity());
        final CommunityService communityService = new CommunityService(this);
        communityService.postWritePost(fcmToken, title, content);
    }

    @Override
    public void getHotPostSuccess(ArrayList<Post> arrayList) {
        hideProgressDialog();
        if(mHotPosts==null || mHotPostAdapter == null){
            showCustomToast("알 수 없는 오류가 발생하였습니다. 다시 시도해주세요");
            return;
        }
        mHotPosts.addAll(arrayList);
        mHotPostAdapter.notifyItemRangeInserted(0, arrayList.size());
    }

    @Override
    public void getNewPostSuccess(ArrayList<Post> arrayList) {
        hideProgressDialog();
        if (arrayList.size() % mSize != 0 || arrayList.size() == 0) {
            mNoMoreItem = true;
        }

        if (arrayList != null) {
            mNewPostAdapter.notifyItemRangeInserted(mNewPosts.size(), arrayList.size());
            mNewPosts.addAll(arrayList);
//            mNewPostAdapter.notifyDataSetChanged();
            mPage += arrayList.size();
            mLoadLock = false;
        }
    }

    @Override
    public void postLikeSuccess(int mode, int position) {
        hideProgressDialog();
        if (mode == 1) {//인기게시글
            mHotPosts.get(position).setLikeCountPlus();
            mHotPostAdapter.notifyItemChanged(position);
        } else {//일반게시글
            mNewPosts.get(position).setLikeCountPlus();
            mNewPostAdapter.notifyItemChanged(position);
        }

    }

    @Override
    public void postWritePostSuccess() {
        hideKeyBoard();
        mEditTextPostWriteContent.setText("");
        mEditTextPostWriteTitle.setText("");
        hideWrite();
        mIsExpanded = false;
        clearAndReLoad();
        hideProgressDialog();
    }

    private void clearAndReLoad() {
        mPage = 0;
        mLoadLock = false;
        mNoMoreItem = false;
        mHotPostAdapter.notifyItemRangeRemoved(0, mHotPosts.size());
        mNewPostAdapter.notifyItemRangeRemoved(0, mNewPosts.size());
        mHotPosts.clear();
        mNewPosts.clear();
        getHotPost();
        getNewPost(mPage);
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        Snackbar.make(getActivity().findViewById(R.id.fragment_community_linear_header),
                message == null || message.isEmpty() ? getString(R.string.network_error) : message, Snackbar.LENGTH_LONG).show();
    }

    public void hideKeyBoard() {
        if (getActivity()!=null){
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 객체 받아오기
            if (imm!=null){
                imm.hideSoftInputFromWindow(mEditTextPostWriteContent.getWindowToken(), 0); // 키보드 숨기기
            }
        }
    }

    @Override
    public void onRefresh() {
        clearAndReLoad();
        swipeRefreshLayout.setRefreshing(false);
    }
}
