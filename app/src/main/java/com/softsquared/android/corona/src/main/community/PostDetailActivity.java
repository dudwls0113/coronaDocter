package com.softsquared.android.corona.src.main.community;

import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageView mImageViewCommentWrite, mImageViewLike, mImageViewBack;
    EditText mEditTextComment;
    View mCommentWrite;
    LikeCheckDialog likeCheckDialog;

    RecyclerView mRecyclerView;
    ArrayList<Comment> comments = new ArrayList<>();
    PostDetailAdapter mPostDetailAdapter;

    int postNo, mLikeCount, mCommentCount;
    String fcmToken;
    boolean mLoadLock = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        mContext = this;
        init();
        getPostDetail(postNo);
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
            mTextViewContent.setText(post.getContent());
            mTextViewLikeCount.setText(post.getLikeCount() + "");
            mTextViewCommentCount.setText(post.getCommentCount() + "");
            mLikeCount = post.getLikeCount();
            mCommentCount = post.getCommentCount();
//
//            String html = post.getContent();
//            HtmlTagHandler tagHandler = new HtmlTagHandler();
//            Spanned styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
//            mTextViewContent.setText(styledText);

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
}
