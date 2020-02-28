package com.softsquared.android.coronadoctor.src.main.info;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import androidx.annotation.NonNull;

import com.softsquared.android.coronadoctor.R;

public class BannerLongDialog extends Dialog {


    public BannerLongDialog(@NonNull Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_banner_long_dialog);
    }
}
