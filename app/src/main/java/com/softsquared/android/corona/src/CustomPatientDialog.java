package com.softsquared.android.corona.src;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.RouteRes;

public class CustomPatientDialog extends Dialog {

    private Context mContext;
    private RouteRes routeRes;

    private TextView mTextViewNum, mTextViewHospital;

    public CustomPatientDialog(@NonNull Context context, RouteRes routeRes) {
        super(context);
        mContext = context;
        this.routeRes = routeRes;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_patient_dialog);
        mTextViewNum = findViewById(R.id.patient_dialog_num);
        mTextViewHospital = findViewById(R.id.patient_dialog_hospital);
        mTextViewHospital.setText(routeRes.getHospital());
    }
}
