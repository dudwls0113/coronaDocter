package com.softsquared.android.coronadoctor.src.splash;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.softsquared.android.coronadoctor.R;


public class MustUpdateDialog extends Dialog {

    private Button mBtnGoUpdate;
    private TextView mBtnCancle;

    private CustomLIstener mCustomLIstener;

    public MustUpdateDialog(Context context, CustomLIstener customLIstener) {
        super(context);
        setContentView(R.layout.custom_dialog_must_update);
        this.mCustomLIstener = customLIstener;

        mBtnGoUpdate = findViewById(R.id.dialog_must_update_ok);
        mBtnGoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomLIstener.goUpdateClick();
                dismiss();
            }
        });

        mBtnCancle = findViewById(R.id.dialog_must_update_cancel);
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomLIstener.cancelClick();
                dismiss();

            }
        });

    }

    public interface CustomLIstener {
        void goUpdateClick();

        void cancelClick();
    }
}
