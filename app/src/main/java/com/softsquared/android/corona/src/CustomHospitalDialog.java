package com.softsquared.android.corona.src;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.map.models.ClinicInfo;
import com.softsquared.android.corona.src.main.map.models.HospitalInfo;

public class CustomHospitalDialog extends Dialog {

    private Context mContext;
    private HospitalInfo hospitalInfo;
    private TextView mTextViewName, mTextViewPhone, mTextViewCount;
    private ImageView mImageViewCall;

    public CustomHospitalDialog(@NonNull Context context, final HospitalInfo hospitalInfo) {
        super(context);
        mContext = context;
        this.hospitalInfo = hospitalInfo;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.custom_hospital_dialog);
        mTextViewName = findViewById(R.id.hospital_name);
        mTextViewCount = findViewById(R.id.hospital_count);
        mTextViewPhone = findViewById(R.id.hospital_phone);
        mImageViewCall = findViewById(R.id.hospital_iv_call);
        mTextViewCount.setText(hospitalInfo.getPatientCount()+"명");
        mTextViewName.setText(hospitalInfo.getName());
        mTextViewName.setSingleLine(true);
        mTextViewName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        mTextViewName.setSelected(true);
        mTextViewPhone.setText(hospitalInfo.getMobile());
        mImageViewCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+hospitalInfo.getMobile()));
                mContext.startActivity(intent);
            }
        });
    }
}
