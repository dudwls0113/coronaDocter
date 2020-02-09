package com.softsquared.android.corona.src.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.community.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.http.POST;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPosts;
    private NewPostAdapterListener mNewPostAdapterListener;

    public NewPostAdapter(Context context, ArrayList<Post> arrayList, NewPostAdapterListener newPostAdapterListener) {
        mContext = context;
        mPosts = arrayList;
        mNewPostAdapterListener = newPostAdapterListener;
    }

    public interface NewPostAdapterListener {
        void click(int postNo, int position);

        void likeClick(int postNo, int position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_new_post, parent, false);
        NewPostAdapter.CustomViewHolder viewHolder = new NewPostAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.mTextViewTitle.setText(mPosts.get(position).getTitle());
        holder.mTextViewContent.setText(mPosts.get(position).getContent());
        holder.mTextViewLikeCount.setText(mPosts.get(position).getLikeCount() + "");
        holder.mTextViewCommentCount.setText(mPosts.get(position).getCommentCount() + "");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdf.format(date);
        try {
            Date nowDate = sdf.parse(formatDate);
            Date registerDate = sdf.parse(mPosts.get(position).getCreatedAt());
            long diff = (nowDate.getTime() - registerDate.getTime())/1000;
            if(diff<60){
                holder.mTextViewTime.setText("방금 전");
            }
            else if((diff/=60)<60){
                holder.mTextViewTime.setText(diff + "분전");
            }
            else if((diff/=60)<24){
                holder.mTextViewTime.setText(diff + "시간전");
            }
            else if((diff/=24)<30){
                holder.mTextViewTime.setText(diff + "일전");
            }
            else if((diff/=30)<12){
                holder.mTextViewTime.setText(diff + "달전");
            }
            else{
                holder.mTextViewTime.setText(diff + "년전");
            }
//            long diff = nowDate.getTime() - registerDate.getTime();
//            if (diff / 60000 < 60) {
//                if (diff / 60000 == 0) {
//                    holder.mTextViewTime.setText("방금 전");
//                } else {
//                    holder.mTextViewTime.setText(diff / 60000 + "분전");
//                }
//            } else if (diff / 108000000 <= 1) {
//                holder.mTextViewTime.setText(diff / 3600000 + "시간전");
//            }
//            else if (diff / 108000000 < 30) {
//                holder.mTextViewTime.setText(diff / 108000000 + "일전");
//            } else {
//                long month = diff / 108000000;
//                holder.mTextViewTime.setText(month / 30 + "달전");
//            }
//            else {
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
//                String registerTime = simpleDateFormat.format(registerDate);
//                holder.mTextViewTime.setText(registerTime);
//            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (null != mPosts ? mPosts.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        View mViewItem;
        TextView mTextViewLikeCount;
        TextView mTextViewCommentCount;
        TextView mTextViewTitle;
        TextView mTextViewContent;
        TextView mTextViewTime;
        ImageButton mImageBtnLike;
        ImageView mImageViewComment;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewItem = itemView.findViewById(R.id.list_new_post_item);
            mViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNewPostAdapterListener.click(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
            mTextViewLikeCount = itemView.findViewById(R.id.list_new_post_tv_like);
            mTextViewCommentCount = itemView.findViewById(R.id.list_new_post_tv_comment);
            mTextViewTitle = itemView.findViewById(R.id.list_new_post_title);
            mTextViewContent = itemView.findViewById(R.id.list_new_post_tv_content);
            mTextViewTime = itemView.findViewById(R.id.list_new_post_time);
            mImageBtnLike = itemView.findViewById(R.id.list_new_post_btn_like);
            mImageViewComment = itemView.findViewById(R.id.list_new_post_iv_comment);

            mImageBtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNewPostAdapterListener.likeClick(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
            mImageViewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNewPostAdapterListener.click(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });

        }
    }
}
