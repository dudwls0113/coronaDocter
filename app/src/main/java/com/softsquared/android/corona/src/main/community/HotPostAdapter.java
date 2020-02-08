package com.softsquared.android.corona.src.main.community;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
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

public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPosts = new ArrayList<>();
    private HotPostAdapterListener mHotPostAdapterListener;

    public HotPostAdapter(Context context, ArrayList<Post> arrayList, HotPostAdapterListener hotPostAdapterListener) {
        mContext = context;
        mPosts = arrayList;
        mHotPostAdapterListener = hotPostAdapterListener;
    }

    public interface HotPostAdapterListener {
        void Click(int postNo, int position);

        void likeClick(int postNo, int position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_hot_post, parent, false);
        HotPostAdapter.CustomViewHolder viewHolder = new HotPostAdapter.CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.mTextViewTitle.setText(mPosts.get(position).getTitle());
        holder.mTextViewContent.setText(mPosts.get(position).getContent());
        holder.mTextViewLikeCount.setText(mPosts.get(position).getLikeCount() + "");
        holder.mTextViewCommentCount.setText(mPosts.get(position).getCommentCount() + "");
        if (mPosts.get(position).getType()>1) {
            holder.mImageViewNotice.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewNotice.setVisibility(View.GONE);
        }
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdf.format(date);
        try {
            Date nowDate = sdf.parse(formatDate);
            Date registerDate = sdf.parse(mPosts.get(position).getCreatedAt());
            long diff = nowDate.getTime() - registerDate.getTime();
            if (diff / 60000 < 60) {
                if (diff / 60000 == 0) {
                    holder.mTextViewTime.setText("방금 전");
                } else {
                    holder.mTextViewTime.setText(diff / 60000 + "분전");
                }
            } else if (diff / 108000000 <= 1) {
                holder.mTextViewTime.setText(diff / 3600000 + "시간전");
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                String registerTime = simpleDateFormat.format(registerDate);
                holder.mTextViewTime.setText(registerTime);
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
    }

    @Override
    public int getItemCount() {
        Log.d("해결", (null != mPosts ? mPosts.size() : 0) + "");
        return (null != mPosts ? mPosts.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        View mViewItem;
        TextView mTextViewLikeCount;
        TextView mTextViewCommentCount;
        TextView mTextViewTitle;
        TextView mTextViewContent;

        TextView mTextViewTime;

        ImageView mImageViewLike, mImageViewNotice;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewItem = itemView.findViewById(R.id.list_hot_post_item);
            mViewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotPostAdapterListener.Click(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
            mTextViewLikeCount = itemView.findViewById(R.id.list_hot_post_tv_like);
            mTextViewCommentCount = itemView.findViewById(R.id.list_hot_post_tv_comment);
            mTextViewTitle = itemView.findViewById(R.id.list_hot_post_title);
            mTextViewContent = itemView.findViewById(R.id.list_hot_post_tv_content);
            mTextViewTime = itemView.findViewById(R.id.list_hot_post_time);
            mImageViewLike = itemView.findViewById(R.id.list_hot_post_iv_like);
            mImageViewNotice = itemView.findViewById(R.id.list_hot_post_tv_notice);
            mImageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotPostAdapterListener.likeClick(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
        }
    }
}
