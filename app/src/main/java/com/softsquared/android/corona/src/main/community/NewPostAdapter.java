package com.softsquared.android.corona.src.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.community.model.Post;

import java.util.ArrayList;

import retrofit2.http.POST;

public class NewPostAdapter extends RecyclerView.Adapter<NewPostAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<Post> mPosts = new ArrayList<>();
    private NewPostAdapterListener mNewPostAdapterListener;

    public NewPostAdapter(Context context, ArrayList<Post> arrayList, NewPostAdapterListener newPostAdapterListener){
        mContext = context;
        mPosts = arrayList;
        mNewPostAdapterListener = newPostAdapterListener;
    }

    public interface NewPostAdapterListener{
        void click();
        void likeClick();
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
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mViewItem = itemView.findViewById(R.id.list_new_post_item);
            mTextViewLikeCount = itemView.findViewById(R.id.list_new_post_tv_like);
            mTextViewCommentCount = itemView.findViewById(R.id.list_new_post_tv_comment);
            mTextViewTitle = itemView.findViewById(R.id.list_new_post_title);
            mTextViewContent = itemView.findViewById(R.id.list_new_post_tv_content);
        }
    }
}
