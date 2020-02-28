package com.softsquared.android.coronadoctor.src.main.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.main.community.model.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.CustomViewHolder> {

    private Context mContext;
    private ArrayList<Comment> comments = new ArrayList<>();

    public PostDetailAdapter(Context context, ArrayList<Comment> arrayList){
        mContext = context;
        comments = arrayList;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_comment, parent, false);
        PostDetailAdapter.CustomViewHolder viewHolder = new PostDetailAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {

        if(position==comments.size()-1){
            holder.mEndLine.setVisibility(View.GONE);
        }
        else{
            holder.mEndLine.setVisibility(View.VISIBLE);
        }

        holder.mTextViewContent.setText(comments.get(position).getComment());

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatDate = sdf.format(date);
        try {
            Date nowDate = sdf.parse(formatDate);
            Date registerDate = sdf.parse(comments.get(position).getCreatedAt());
            long diff = nowDate.getTime() - registerDate.getTime();
            if (diff / 60000 < 60) {
                if (diff / 60000 == 0) {
                    holder.mTextViewTime.setText("방금 전");
                } else {
                    holder.mTextViewTime.setText(diff / 60000 + "분전");
                }
            } else if (diff / 108000000 <= 1) {
                holder.mTextViewTime.setText(diff / 3600000 + "시간전");
            }
            else{
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
        return (comments==null ? 0 : comments.size());
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView mTextViewTime;
        TextView mTextViewContent;
        View mEndLine;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewTime = itemView.findViewById(R.id.list_comment_tv_time);
            mTextViewContent = itemView.findViewById(R.id.list_comment_tv_content);
            mEndLine = itemView.findViewById(R.id.endLine);
        }
    }
}
