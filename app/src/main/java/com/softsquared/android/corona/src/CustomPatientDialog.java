package com.softsquared.android.corona.src;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import androidx.annotation.NonNull;

import com.softsquared.android.corona.R;

public class CustomPatientDialog extends Dialog {

    private Context mContext;

    public CustomPatientDialog(@NonNull Context context) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_patient_dialog);
    }
}
