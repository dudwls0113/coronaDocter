package com.softsquared.android.corona.src;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.ClinicInfo;

public class CustomClinicDialog extends Dialog {

    private Context mContext;
    private ClinicInfo clinicInfo;
    private TextView mTextViewName, mTextViewPhone;
    private ImageView mImageViewCall;

    public CustomClinicDialog(@NonNull Context context, final ClinicInfo clinicInfo) {
        super(context);
        mContext = context;
        this.clinicInfo = clinicInfo;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_clinic_dialog);
        mTextViewName = findViewById(R.id.clinic_name);
        mTextViewPhone = findViewById(R.id.clinic_phone);
        mImageViewCall = findViewById(R.id.clinic_iv_call);
        mTextViewName.setText(clinicInfo.getName());
        mTextViewName.setSingleLine(true);
        mTextViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTextViewName.setSelected(true);
        mTextViewPhone.setText(clinicInfo.getMobile());
        mImageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+clinicInfo.getMobile()));
                mContext.startActivity(intent);
            }
        });
    }
}
