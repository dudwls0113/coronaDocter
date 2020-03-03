package com.softsquared.android.corona.src.main.map;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.Infected;

import java.util.ArrayList;

public class InfectedSelectCustomDialog extends Dialog {

    private LinearLayout mLinearAllSelect;
    private Button mTextViewOk;

    private ImageView mImageViewAllSelect;
    private RecyclerView mRecyclerViewInfected;
    private InfectedSelectAdapter infectedSelectAdapter;
//    private ExpandableInfectedSelectAdapter infectedSelectAdapter;

    private ArrayList<Infected> mArrayListInfected;
    private LinearLayoutManager mLayoutManager;

    private CustomDialogDoneEventListener customDialogDoneEventListener;

    Context mContext;
    boolean isAllSelected;

    @SuppressLint("WrongConstant")
    public InfectedSelectCustomDialog(Context context, final ArrayList<Infected> arrayListInfected, final CustomDialogDoneEventListener onCustomDialogEventListener) {
        super(context);
        mContext = context;
        this.mArrayListInfected = arrayListInfected;
        this.customDialogDoneEventListener = onCustomDialogEventListener;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.custom_patient_select_dialog);     //다이얼로그에서 사용할 레이아웃입니다.

        mLinearAllSelect = findViewById(R.id.dialog_infect_select_linear_all_select);
        mImageViewAllSelect = findViewById(R.id.dialog_infect_select_iv_btn);
        mRecyclerViewInfected = findViewById(R.id.dialog_infect_select_rv);
        mTextViewOk = findViewById(R.id.dialog_infect_select_tv_ok);


        infectedSelectAdapter = new InfectedSelectAdapter(mArrayListInfected, mContext, new InfectedSelectAdapter.AdapterListener() {
            @Override
            public void clickCheckedBtn(int index) {
                if (mArrayListInfected.get(index).isSelected()) {
                    mArrayListInfected.get(index).setSelected(false);
                } else {
                    mArrayListInfected.get(index).setSelected(true);
                }
                infectedSelectAdapter.notifyItemChanged(index);
                if (isAllChecked()) {
                    mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_on);
                    isAllSelected = true;
                    infectedSelectAdapter.isAllSelected = true;
                } else {
                    mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_off);
                    isAllSelected = false;
                    infectedSelectAdapter.isAllSelected = false;
                }
            }
        });


        mLayoutManager = new LinearLayoutManager(mContext, LinearLayout.VERTICAL, false);
        mRecyclerViewInfected.setLayoutManager(mLayoutManager);
        mRecyclerViewInfected.setAdapter(infectedSelectAdapter);

        mLinearAllSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infectedSelectAdapter.allSelect();
                if (isAllSelected) {
                    mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_off);
                    isAllSelected = false;
                } else {
                    mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_on);
                    isAllSelected = true;
                }
            }
        });
        mTextViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogDoneEventListener.doneClicked(mArrayListInfected);
                dismiss();
            }
        });

        if (isAllChecked()) {
            mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_on);
            isAllSelected = true;
            infectedSelectAdapter.isAllSelected = true;
        } else {
            mImageViewAllSelect.setImageResource(R.drawable.btn_check_box_off);
            isAllSelected = false;
            infectedSelectAdapter.isAllSelected = false;
        }
    }

    public boolean isAllChecked() {
        for (int i = 0; i < mArrayListInfected.size(); i++) {
            if (!mArrayListInfected.get(i).isSelected()) {
                return false;
            }
        }
        return true;
    }

    public interface CustomDialogDoneEventListener {
        public void doneClicked(ArrayList<Infected> infectedArrayList);
    }

}
