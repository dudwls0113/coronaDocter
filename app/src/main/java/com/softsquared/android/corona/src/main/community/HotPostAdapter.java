package com.softsquared.android.corona.src.main.community;

import android.content.Context;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.community.model.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HotPostAdapter extends RecyclerView.Adapter<HotPostAdapter.CustomViewHolder> {

    public interface HotPostAdapterListener {
        void Click(int postNo, int position);

        void likeClick(int postNo, int position);
    }

    private Context mContext;
    private ArrayList<Post> mPosts = new ArrayList<>();
    private HotPostAdapterListener mHotPostAdapterListener;


    public HotPostAdapter(Context context, ArrayList<Post> arrayList, HotPostAdapterListener hotPostAdapterListener) {
        mContext = context;
        mPosts = arrayList;
        mHotPostAdapterListener = hotPostAdapterListener;
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
        Post post = mPosts.get(position);
        holder.mTextViewTitle.setText(post.getTitle());
        holder.mTextViewLikeCount.setText(post.getLikeCount() + "");
        holder.mTextViewCommentCount.setText(post.getCommentCount() + "");
        if (post.getType() > 1) {
            holder.mImageViewNotice.setVisibility(View.VISIBLE);
        } else {
            holder.mImageViewNotice.setVisibility(View.GONE);
        }

        if(post.getHtmlContent() == null){
            holder.mTextViewContent.setText(mPosts.get(position).getContent());
        }
        else if(post.getHtmlContent().length() <2){
            holder.mTextViewContent.setText(mPosts.get(position).getContent());
        }
        else{//html가능
            String html = mPosts.get(position).getHtmlContent();
            HtmlTagHandler tagHandler = new HtmlTagHandler();
            Spanned styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
            holder.mTextViewContent.setText(styledText);
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

//        String html = mPosts.get(position).getContent();
//        HtmlTagHandler tagHandler = new HtmlTagHandler();
//        Spanned styledText = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY, null, tagHandler);
//        holder.mTextViewContent.setText(styledText);

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

//            if (diff / 60000 < 60) {
//                if (diff / 60000 == 0) {
//                    holder.mTextViewTime.setText("방금 전");
//                } else {
//                    holder.mTextViewTime.setText(diff / 60000 + "분전");
//                }
//            } else if (diff / 108000000 <= 1) {
//                holder.mTextViewTime.setText(diff / 3600000 + "시간전");
//            } else if (diff / 108000000 < 30) {
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

        LinearLayout linearLayout;
        TextView mTextViewLikeCount;
        TextView mTextViewCommentCount;
        TextView mTextViewTitle;
        TextView mTextViewContent;

        TextView mTextViewTime;

        ImageView mImageViewNotice, mImageViewComment;

        ImageButton mImageBtnLike;
        ImageView mImageViewThumbnail;


        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.list_hot_post_item);
            linearLayout.setOnClickListener(new View.OnClickListener() {
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
            mImageBtnLike = itemView.findViewById(R.id.list_hot_post_iv_like);
            mImageViewNotice = itemView.findViewById(R.id.list_hot_post_tv_notice);
            mImageViewComment = itemView.findViewById(R.id.list_hot_post_iv_comment);
            mImageViewThumbnail = itemView.findViewById(R.id.list_hot_post_iv);

            mImageBtnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotPostAdapterListener.likeClick(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
            mImageViewComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mHotPostAdapterListener.Click(mPosts.get(getAdapterPosition()).getPostNo(), getAdapterPosition());
                }
            });
        }
    }
}
