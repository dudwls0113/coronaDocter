package com.softsquared.android.coronadoctor.src.main.community;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.main.community.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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

        void reportClick(int postNo, int position);
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_new_post, parent, false);
        NewPostAdapter.CustomViewHolder viewHolder = new NewPostAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, int position) {
        Post post = mPosts.get(position);
        holder.mTextViewTitle.setText(post.getTitle());
        holder.mTextViewContent.setText(post.getContent());
        holder.mTextViewLikeCount.setText(post.getLikeCount() + "");
        holder.mTextViewCommentCount.setText(post.getCommentCount() + "");
        if (post.getType()>1){
            holder.mImageViewReport.setVisibility(View.GONE);
        } else{
            holder.mImageViewReport.setVisibility(View.VISIBLE);
        }

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

            if(post.getHtmlContent() == null){
                holder.mTextViewContent.setText(mPosts.get(position).getContent());
                holder.mImageViewReport.setVisibility(View.VISIBLE);
            }
            else if(post.getHtmlContent().length() <2){
                holder.mTextViewContent.setText(mPosts.get(position).getContent());
                holder.mImageViewReport.setVisibility(View.VISIBLE);
            }
            else{//html가능
                String html = mPosts.get(position).getHtmlContent();
                HtmlTagHandler tagHandler = new HtmlTagHandler();
                Spanned styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
                holder.mTextViewContent.setText(styledText);
                holder.mImageViewReport.setVisibility(View.GONE);
            }

            if(post.getImageUrl()!=null){
                RequestOptions sharedOptions =
                        new RequestOptions()
                                .placeholder(R.drawable.bg_round)
                                .error(R.drawable.bg_round)
                                .diskCacheStrategy(DiskCacheStrategy.DATA)
//                            .centerCrop()
                        ;
                Glide.with(mContext).load(post.getImageUrl()).apply(sharedOptions).thumbnail(0.1f).into(holder.mImageViewThumbnail);
            }
            else{
                holder.mImageViewThumbnail.setVisibility(View.GONE);
            }


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
        ImageView mImageViewThumbnail;
        ImageView mImageViewReport;

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
            mImageViewThumbnail = itemView.findViewById(R.id.list_post_iv);

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
            mImageViewReport = itemView.findViewById(R.id.list_new_post_iv_report);
            mImageViewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNewPostAdapterListener.reportClick(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
        }
    }
}
