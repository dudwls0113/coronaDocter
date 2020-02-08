package com.softsquared.android.corona.src.main.community;

import android.content.Context;
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

import java.util.ArrayList;

public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPosts = new ArrayList<>();
    private HotPostAdapterListener mHotPostAdapterListener;

    public HotPostAdapter(Context context, ArrayList<Post> arrayList, HotPostAdapterListener hotPostAdapterListener){
        mContext = context;
        mPosts = arrayList;
        mHotPostAdapterListener = hotPostAdapterListener;
    }

    public interface HotPostAdapterListener{
        void Click();
        void likeClick();
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
        holder.mTextViewLikeCount.setText(mPosts.get(position).getLikeCount()+"");
        holder.mTextViewCommentCount.setText(mPosts.get(position).getCommentCount()+"");

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
        ImageView mImageViewLike;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewItem = itemView.findViewById(R.id.list_hot_post_item);
            mTextViewLikeCount = itemView.findViewById(R.id.list_hot_post_tv_like);
            mTextViewCommentCount = itemView.findViewById(R.id.list_hot_post_tv_comment);
            mTextViewTitle = itemView.findViewById(R.id.list_hot_post_title);
            mTextViewContent = itemView.findViewById(R.id.list_hot_post_tv_content);
            mImageViewLike = itemView.findViewById(R.id.list_hot_post_iv_like);
            mImageViewLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotPostAdapterListener.likeClick();
                }
            });
        }
    }
}
