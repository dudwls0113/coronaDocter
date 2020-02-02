package com.softsquared.android.corona.src;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.RouteRes;

public class CustomPatientDialog extends Dialog {

    private Context mContext;
    private RouteRes routeRes;
    private View mView;

    private TextView mTextViewNum, mTextViewHospital, mTextVewDate, mTextVewIsolationDate, mTextVewPlace, mTextVewContent, mTextVewAge, mTextVewCount;

    public CustomPatientDialog(@NonNull Context context, RouteRes routeRes) {
        super(context);
        mContext = context;
        this.routeRes = routeRes;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_patient_dialog);
        mView = findViewById(R.id.patient_dialog);
        GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.bg_round);
        mView.setBackground(drawable);
        mView.setClipToOutline(true);
        mTextViewNum = findViewById(R.id.patient_dialog_num);
        mTextViewHospital = findViewById(R.id.patient_dialog_hospital);
        mTextVewDate = findViewById(R.id.patient_dialog_date);
        mTextVewIsolationDate = findViewById(R.id.patient_dialog_isolationDate);
        mTextVewPlace = findViewById(R.id.patient_dialog_place);
        mTextVewContent = findViewById(R.id.patient_dialog_content);
        mTextVewAge = findViewById(R.id.patient_dialog_age);
        mTextVewCount = findViewById(R.id.patient_dialog_count);
        mTextViewNum.setText(routeRes.getName()+"번 환자");
        mTextViewHospital.setText(routeRes.getHospital());
        mTextVewDate.setText(routeRes.getDate());
        mTextVewIsolationDate.setText(routeRes.getIsolationDate());
        mTextVewPlace.setText(routeRes.getRouteName());
        mTextVewContent.setText(routeRes.getContent());
        mTextVewAge.setText(routeRes.getAge()+"살");
        mTextVewCount.setText(routeRes.getContactCount()+"명");
    }
}
