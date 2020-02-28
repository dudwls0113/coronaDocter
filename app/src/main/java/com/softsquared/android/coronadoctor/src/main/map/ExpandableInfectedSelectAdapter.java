package com.softsquared.android.coronadoctor.src.main.map;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.main.map.models.Infected;

import java.util.ArrayList;


public class ExpandableInfectedSelectAdapter extends RecyclerView.Adapter<ExpandableInfectedSelectAdapter.ViewHolder> {

    private ArrayList<Infected> mArrayListInfected;
    private Context mContext;
    private AdapterListener adapterListener;
    public boolean isAllSelected = true;

    // Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;


    public ExpandableInfectedSelectAdapter(ArrayList<Infected> items, Context context, AdapterListener adapterListener) {
        this.mArrayListInfected = items;
        this.mContext = context;
        this.adapterListener = adapterListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_select_expandable_infected, viewGroup, false);
        //txtName.setText(items.get(position));
        return new ViewHolder(v);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Infected item = mArrayListInfected.get(position);
        viewHolder.mTextViewName.setText(item.getName());

        viewHolder.mLinearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;

//                    adapterListener.clickCheckedBtn(getAdapterPosition());
            }
        });
        viewHolder.changeVisibility(selectedItems.get(position));

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

//        ImageView mImageViewSelect;
        TextView mTextViewName;
//        , mTextViewNew, mTextViewCured;
        LinearLayout mLinearItem;
        RecyclerView expandable;
        InExpandableSelectAdapter inExpandableSelectAdapter;

        public ViewHolder(View itemView) {
            super(itemView);
//            this.mTextViewNew = itemView.findViewById(R.id.dialog_infect_select_tv_new);
//            this.mImageViewSelect = itemView.findViewById(R.id.dialog_infect_select_iv_btn);
            this.mTextViewName = itemView.findViewById(R.id.dialog_infect_select_tv_region);
//            this.mTextViewCured = itemView.findViewById(R.id.dialog_infect_select_tv_cured);
            this.mLinearItem = itemView.findViewById(R.id.list_infected_Linear_item);
            this.expandable = itemView.findViewById(R.id.dialog_infect_expandable);

            ArrayList<Infected> regionInfectedList = new ArrayList<>();

            this.inExpandableSelectAdapter = new InExpandableSelectAdapter(mArrayListInfected, mContext, new InExpandableSelectAdapter.AdapterListener() {
                @Override
                public void clickUnCheckedBtn(int index) {

                }

                @Override
                public void clickCheckedBtn(int index) {
                    if (mArrayListInfected.get(index).isSelected()) {
                        mArrayListInfected.get(index).setSelected(false);
                    } else {
                        mArrayListInfected.get(index).setSelected(true);
                    }
                    inExpandableSelectAdapter.notifyItemChanged(index);
//                    if (isAllChecked()) {
//                        mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_on);
//                        isAllSelected = true;
//                        infectedSelectAdapter.isAllSelected = true;
//                    } else {
//                        mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_off);
//                        isAllSelected = false;
//                        infectedSelectAdapter.isAllSelected = false;
//                    }
                }
            });

        }


        /**
         * 클릭된 Item의 상태 변경
         * @param isExpanded Item을 펼칠 것인지 여부
         */
        private void changeVisibility(final boolean isExpanded) {
            // height 값을 dp로 지정해서 넣고싶으면 아래 소스를 이용
            int dpValue = 150;
            float d = mContext.getResources().getDisplayMetrics().density;
            int height = (int) (dpValue * d);

            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, height) : ValueAnimator.ofInt(height, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // value는 height 값
                    int value = (int) animation.getAnimatedValue();
                    // imageView의 높이 변경
                    expandable.getLayoutParams().height = value;
                    expandable.requestLayout();
//                 imageView가 실제로 사라지게하는 부분
                    expandable.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();
        }

    }

    public interface AdapterListener {
//        void clickUnCheckedBtn(int index);

        void clickCheckedBtn(int index);

    }

    public void allSelect(){
        if(isAllSelected){
            //모두 해제
            for(int i=0; i<mArrayListInfected.size(); i++){
                mArrayListInfected.get(i).setSelected(false);
            }
            notifyItemRangeChanged(0, mArrayListInfected.size());
            isAllSelected=false;
        }
        else{
            //모두 선택
            for(int i=0; i<mArrayListInfected.size(); i++){
                mArrayListInfected.get(i).setSelected(true);
            }
            notifyItemRangeChanged(0, mArrayListInfected.size());
            isAllSelected=true;
       }
    }



}