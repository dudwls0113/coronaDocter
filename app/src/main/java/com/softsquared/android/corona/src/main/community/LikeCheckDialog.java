package com.softsquared.android.corona.src.main.community;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.softsquared.android.corona.R;


public class LikeCheckDialog extends Dialog {

    private Button mBtnGoUpdate;
    private TextView mBtnCancle;

    private CustomLIstener mCustomLIstener;

    public LikeCheckDialog(Context context, CustomLIstener customLIstener) {
        super(context);
        setContentView(R.layout.custom_dialog_like_check);
        this.mCustomLIstener = customLIstener;

        mBtnGoUpdate = findViewById(R.id.dialog_like_check_ok);
        mBtnGoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomLIstener.okClick();
                dismiss();
            }
        });

        mBtnCancle = findViewById(R.id.dialog_like_check_cancel);
        mBtnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomLIstener.cancelClick();
                dismiss();

            }
        });

    }

    public interface CustomLIstener {
        void okClick();

        void cancelClick();
    }
}
