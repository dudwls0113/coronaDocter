package com.softsquared.android.corona.src.main.community;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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

public class CommunityDetailActivity extends BaseActivity implements PostDetailView {

    Context mContext;

    TextView mTextViewTitle, mTextViewTime, mTextViewContent, mTextViewLikeCount, mTextViewCommentCount;
    ImageView mImageViewCommentWrite;
    EditText mEdtComment;
    View mCommentWrite;

    RecyclerView mRecyclerView;
    ArrayList<Comment> comments = new ArrayList<>();
    PostDetailAdapter mPostDetailAdapter;

    int postNo;
    String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_detail);
        mContext = this;
        init();
        getPostDetail(postNo);
    }

    void init(){
        postNo = getIntent().getIntExtra("postNo",0);

        SharedPreferences spf = sSharedPreferences;
        fcmToken = spf.getString("fcm", null);

        mTextViewTitle = findViewById(R.id.detail_title);
        mTextViewTime = findViewById(R.id.detail_time);
        mTextViewContent = findViewById(R.id.detail_content);
        mTextViewLikeCount = findViewById(R.id.detail_tv_like);
        mTextViewCommentCount = findViewById(R.id.detail_tv_comment);

        mEdtComment = findViewById(R.id.comment_edt_write);

        mImageViewCommentWrite = findViewById(R.id.comment_write);
        mImageViewCommentWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mEdtComment.getText().toString().length()<2){
                    showCustomToast("댓글은 2글자 이상 입력해주세요.");
                }
                else{
                    postCommentWrite(fcmToken, postNo, mEdtComment.getText().toString());
                }
            }
        });

        mRecyclerView = findViewById(R.id.detail_comment_rv);
        mPostDetailAdapter = new PostDetailAdapter(mContext,comments);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this){
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
    }

    void getPostDetail(int postNo){
        showProgressDialog();
        final PostDetailService postDetailService = new PostDetailService(this);
        postDetailService.getPostDetail(postNo);
    }

    void postCommentWrite(String fcmToken, int postNo, String content){
        showProgressDialog();
        final PostDetailService postDetailService = new PostDetailService(this);
        postDetailService.postCommentWrite(fcmToken, postNo, content);
    }

    @Override
    public void getPostDetail(Post post, ArrayList<Comment> arrayList) {
        System.out.println("댓글 사이즈: " + arrayList.size());
        hideProgressDialog();
        mTextViewTitle.setText(post.getTitle());
        mTextViewContent.setText(post.getContent());
        mTextViewLikeCount.setText(post.getLikeCount()+"");
        mTextViewCommentCount.setText(post.getCommentCount()+"");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdf.format(date);
        try {
            Date nowDate = sdf.parse(formatDate);
            Date registerDate = sdf.parse(post.getCreatedAt());
            long diff = nowDate.getTime() - registerDate.getTime();
            if (diff / 60000 < 60) {
                if (diff / 60000 == 0) {
                    mTextViewTime.setText("방금 전");
                } else {
                   mTextViewTime.setText(diff / 60000 + "분전");
                }
            } else if (diff / 108000000 <= 1) {
                mTextViewTime.setText(diff / 3600000 + "시간전");
            }
            else{
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                String registerTime = simpleDateFormat.format(registerDate);
                mTextViewTime.setText(registerTime);
            }
//            } else if (diff / 108000000 < 30) {
//                holder.mTextViewTime.setText(diff / 108000000 + "일전");
//            } else {
//                long month = diff / 108000000;
//                holder.mTextViewTime.setText(month / 30 + "달전");
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        comments.addAll(arrayList);
        mPostDetailAdapter.notifyDataSetChanged();
    }

    @Override
    public void postCommentWrite() {
        hideProgressDialog();
        mEdtComment.setText("");
        comments.clear();
        mPostDetailAdapter.notifyDataSetChanged();
        getPostDetail(postNo);
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        showCustomToast( message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }
}
