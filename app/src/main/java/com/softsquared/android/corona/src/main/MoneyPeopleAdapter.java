package com.softsquared.android.corona.src.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.Infected;

import java.util.ArrayList;


public class MoneyPeopleAdapter extends RecyclerView.Adapter<MoneyPeopleAdapter.ViewHolder> {

    private ArrayList<String> mArrayListName;
    private Context mContext;

    public MoneyPeopleAdapter(ArrayList<String> items, Context context) {
        this.mArrayListName = items;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_money_people, viewGroup, false);
        //txtName.setText(items.get(position));
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final String item = mArrayListName.get(position);
        viewHolder.mTextViewName.setText(item);
    }

    @Override
    public int getItemCount() {
        return mArrayListName.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextViewName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewName = itemView.findViewById(R.id.dialog_money_tv_new);
        }
    }
}