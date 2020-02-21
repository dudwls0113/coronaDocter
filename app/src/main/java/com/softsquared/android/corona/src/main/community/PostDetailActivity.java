package com.softsquared.android.corona.src.main.community;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.main.community.interfaces.PostDetailView;
import com.softsquared.android.corona.src.main.community.model.Comment;
import com.softsquared.android.corona.src.main.community.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;

public class PostDetailActivity extends BaseActivity implements PostDetailView {

    Context mContext;

    TextView mTextViewTitle, mTextViewTime, mTextViewContent, mTextViewLikeCount, mTextViewCommentCount;
    ImageView mImageViewCommentWrite, mImageViewLike, mImageViewBack, mImageViewImg;
    EditText mEditTextComment;
    View mCommentWrite;
    LikeCheckDialog likeCheckDialog;

    RecyclerView mRecyclerView;
    ArrayList<Comment> comments = new ArrayList<>();
    PostDetailAdapter mPostDetailAdapter;

    int postNo, mLikeCount, mCommentCount;
    String fcmToken;
    boolean mLoadLock = false;

    private AdView mAdView;
    private FrameLayout mFrame;

    public static final String AD_TEST_KEY_BANNER = "ca-app-pub-3940256099942544/6300978111";

    //      [주의] 실제키로 빌드하면안됨
//    public static final String AD_REAL_KEY_BANNER = "ca-app-pub-2165488373168832/6786768344";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        mContext = this;
        init();
        getPostDetail(postNo);

        mAdView = new AdView(this);
        mAdView.setAdUnitId(AD_TEST_KEY_BANNER);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mFrame = findViewById(R.id.activity_post_detail_frame);
        // Step 1 - Create AdView and set the ad unit ID on it.
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM;
        mFrame.addView(mAdView, params);
        loadBanner();
    }

    void init() {
        if (getIntent() == null) {
            finish();
        } else {
            postNo = getIntent().getIntExtra("postNo", 0);
        }

        SharedPreferences spf = sSharedPreferences;
        fcmToken = spf.getString("fcm", null);

        mTextViewTitle = findViewById(R.id.detail_title);
        mTextViewTime = findViewById(R.id.detail_time);
        mTextViewContent = findViewById(R.id.detail_content);
        mTextViewLikeCount = findViewById(R.id.detail_tv_like);
        mTextViewCommentCount = findViewById(R.id.detail_tv_comment);
        mImageViewLike = findViewById(R.id.activity_post_detail_iv_like);
        mImageViewBack = findViewById(R.id.community_detail_back);

        mEditTextComment = findViewById(R.id.comment_edt_write);

        mImageViewCommentWrite = findViewById(R.id.comment_write);
        mImageViewCommentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEditTextComment.getText().toString().length() < 2) {
                    showCustomToast("댓글은 2글자 이상 입력해주세요.");
                } else if (fcmToken == null) {
                    showCustomToast("올바르지 않은 접근입니다.");
                } else {
                    postCommentWrite(fcmToken, postNo, mEditTextComment.getText().toString());
                }
            }
        });

        mRecyclerView = findViewById(R.id.detail_comment_rv);
        mPostDetailAdapter = new PostDetailAdapter(mContext, comments);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollHorizontally() {
                return false;
            }

            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mRecyclerView.setAdapter(mPostDetailAdapter);

        mImageViewLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeCheckDialog = new LikeCheckDialog(mContext, new LikeCheckDialog.CustomLIstener() {
                    @Override
                    public void okClick() {
                        postLike(postNo);
                    }

                    @Override
                    public void cancelClick() {

                    }
                });
                likeCheckDialog.show();
            }
        });
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mEditTextComment.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mEditTextComment.getText().toString().length() != 0) {
                        if (mEditTextComment.getText().toString().length() < 2) {
                            showCustomToast("댓글은 2글자 이상 입력해주세요.");
                        } else if (fcmToken == null) {
                            showCustomToast("올바르지 않은 접근입니다.");
                        } else {
                            postCommentWrite(fcmToken, postNo, mEditTextComment.getText().toString());
                        }
//                        postCommentWrite(fcmToken, postNo, mEditTextComment.getText().toString());
                    }
                }
                return false;
            }
        });

        mImageViewImg = findViewById(R.id.community_detail_iv_img);
    }

    void getPostDetail(int postNo) {
        showProgressDialog();
        final PostDetailService postDetailService = new PostDetailService(this);
        postDetailService.getPostDetail(postNo);
    }

    void postCommentWrite(String fcmToken, int postNo, String content) {
        if (!mLoadLock) {
            mLoadLock = true;
            showProgressDialog();
            final PostDetailService postDetailService = new PostDetailService(this);
            postDetailService.postCommentWrite(fcmToken, postNo, content);
        }
    }

    void postLike(int postNo) {
        showProgressDialog();
        final PostDetailService postDetailService = new PostDetailService(this);
        postDetailService.postLike(postNo, fcmToken);
    }

    @Override
    public void getPostDetail(Post post, ArrayList<Comment> arrayList) {
//        System.out.println("댓글 사이즈: " + arrayList.size());
        hideProgressDialog();
        if (post != null) {
            mTextViewTitle.setText(post.getTitle());
            mTextViewLikeCount.setText(post.getLikeCount() + "");
            mTextViewCommentCount.setText(post.getCommentCount() + "");
            mLikeCount = post.getLikeCount();
            mCommentCount = post.getCommentCount();
            RequestOptions sharedOptions =
                    new RequestOptions()
                            .override(600, 600)
                            .placeholder(R.drawable.bg_round)
                            .error(R.drawable.bg_round)
                            .diskCacheStrategy(DiskCacheStrategy.DATA)
                            .centerCrop();
            if (post.getImageUrl()!=null){
                mImageViewImg.setVisibility(View.VISIBLE);
                Glide.with(this).load(post.getImageUrl()).apply(sharedOptions).into(mImageViewImg);
            }
            else{
                mImageViewImg.setVisibility(View.VISIBLE);
            }
//
            String html;
            if(post.getHtmlContent() == null){
                mTextViewContent.setText(post.getContent());
            }
            else if(post.getHtmlContent().length() <2){
                mTextViewContent.setText(post.getContent());
            }
            else{//html가능
                html = post.getHtmlContent();
                HtmlTagHandler tagHandler = new HtmlTagHandler();
                Spanned styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
                mTextViewContent.setText(styledText);
            }

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formatDate = sdf.format(date);
            try {
                Date nowDate = sdf.parse(formatDate);
                Date registerDate = sdf.parse(post.getCreatedAt());
                long diff = 0;
                if (nowDate != null && registerDate != null) {
//                    diff = nowDate.getTime() - registerDate.getTime();
                    diff = (nowDate.getTime() - registerDate.getTime()) / 1000;
                    if (diff < 60) {
                        mTextViewTime.setText("방금 전");
                    } else if ((diff /= 60) < 60) {
                        mTextViewTime.setText(diff + "분전");
                    } else if ((diff /= 60) < 24) {
                        mTextViewTime.setText(diff + "시간전");
                    } else if ((diff /= 24) < 30) {
                        mTextViewTime.setText(diff + "일전");
                    } else if ((diff /= 30) < 12) {
                        mTextViewTime.setText(diff + "달전");
                    } else {
                        mTextViewTime.setText(diff + "년전");
                    }
//                long diff = 0;
//                if (nowDate!=null && registerDate!=null){
//                    diff = nowDate.getTime() - registerDate.getTime();
//                }
//                if (diff / 60000 < 60) {
//                    if (diff / 60000 == 0) {
//                        mTextViewTime.setText("방금 전");
//                    } else {
//                        mTextViewTime.setText(diff / 60000 + "분전");
//                    }
//                } else if (diff / 108000000 <= 1) {
//                    mTextViewTime.setText(diff / 3600000 + "시간전");
//                } else if (diff / 108000000 < 30) {
//                    mTextViewTime.setText(diff / 108000000 + "일전");
//                } else {
//                    long month = diff / 108000000;
//                    mTextViewTime.setText(month / 30 + "달전");
//                }
////                else {
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
//                    String registerTime = simpleDateFormat.format(registerDate);
//                    mTextViewTime.setText(registerTime);
//                }
//            } else if (diff / 108000000 < 30) {
//                holder.mTextViewTime.setText(diff / 108000000 + "일전");
//            } else {
//                long month = diff / 108000000;
//                holder.mTextViewTime.setText(month / 30 + "달전");
//            }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (arrayList != null) {
            comments.clear();
            comments.addAll(arrayList);
            mPostDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void postCommentWrite() {
        hideProgressDialog();
        hideKeyBoard();
        mEditTextComment.setText("");
//        mPostDetailAdapter.notifyItemRangeRemoved(0, comments.size());
//        comments.clear();
        getPostDetail(postNo);
        mCommentCount++;
        mTextViewCommentCount.setText(String.valueOf(mCommentCount));
        mLoadLock = false;
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        Snackbar.make(findViewById(R.id.activity_post_detail_iv_like),
                message == null || message.isEmpty() ? getString(R.string.network_error) : message, Snackbar.LENGTH_LONG).show();
        mLoadLock = false;
    }

    @Override
    public void postLikeSuccess() {
        hideProgressDialog();
        mLikeCount++;
        mTextViewLikeCount.setText(String.valueOf(mLikeCount));
    }

    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); // 키보드 객체 받아오기
        imm.hideSoftInputFromWindow(mEditTextComment.getWindowToken(), 0); // 키보드 숨기기
    }

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        mAdView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
