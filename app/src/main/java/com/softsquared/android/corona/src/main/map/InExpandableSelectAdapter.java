package com.softsquared.android.corona.src.main.map;

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


public class InExpandableSelectAdapter extends RecyclerView.Adapter<InExpandableSelectAdapter.ViewHolder> {

    private ArrayList<Infected> mArrayListInfected;
    private Context mContext;
    private AdapterListener adapterListener;
    public boolean isAllSelected = true;

    public InExpandableSelectAdapter(ArrayList<Infected> items, Context context, AdapterListener adapterListener) {
        this.mArrayListInfected = items;
        this.mContext = context;
        this.adapterListener = adapterListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_select_infected, viewGroup, false);
        //txtName.setText(items.get(position));
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Infected item = mArrayListInfected.get(position);
        if (item.getIsUpdated() == 1) {
            viewHolder.mTextViewNew.setVisibility(View.VISIBLE);
        } else {
            viewHolder.mTextViewNew.setVisibility(View.GONE);
        }
        if (item.getIsCured() == 1){
            viewHolder.mTextViewCured.setVisibility(View.VISIBLE);
        } else{
            viewHolder.mTextViewCured.setVisibility(View.GONE);
        }
        if (item.isSelected()) {
            viewHolder.mImageViewSelect.setImageResource(R.drawable.btn_check_box_on);
        } else {
            viewHolder.mImageViewSelect.setImageResource(R.drawable.btn_check_box_off);
        }
        viewHolder.mTextViewName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return mArrayListInfected.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mImageViewSelect;
        TextView mTextViewName, mTextViewNew, mTextViewCured;
        LinearLayout mLinearItem;
        public ViewHolder(View itemView) {
            super(itemView);
            this.mTextViewNew = itemView.findViewById(R.id.dialog_infect_select_tv_new);
            this.mImageViewSelect = itemView.findViewById(R.id.dialog_infect_select_iv_btn);
            this.mTextViewName = itemView.findViewById(R.id.dialog_infect_select_tv_name);
            this.mTextViewCured = itemView.findViewById(R.id.dialog_infect_select_tv_cured);
            this.mLinearItem = itemView.findViewById(R.id.list_infected_Linear_item);
            this.mLinearItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterListener.clickCheckedBtn(getAdapterPosition());
                }
            });
        }
    }

    public interface AdapterListener {
        void clickUnCheckedBtn(int index);

        void clickCheckedBtn(int index);

    }

//    public void allSelect(){
//        if(isAllSelected){
//            //모두 해제
//            for(int i=0; i<mArrayListInfected.size(); i++){
//                mArrayListInfected.get(i).setSelected(false);
//            }
//            notifyItemRangeChanged(0, mArrayListInfected.size());
//            isAllSelected=false;
//        }
//        else{
//            //모두 선택
//            for(int i=0; i<mArrayListInfected.size(); i++){
//                mArrayListInfected.get(i).setSelected(true);
//            }
//            notifyItemRangeChanged(0, mArrayListInfected.size());
//            isAllSelected=true;
//       }
//    }
}