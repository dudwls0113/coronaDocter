package com.softsquared.android.coronadoctor.src.splash;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.softsquared.android.coronadoctor.R;

public class UpdateDialog extends Dialog {

    private TextView mTextViewOk;
    private TextView mTextViewCancel;

    private CustomLIstener mCustomLIstener;

    public UpdateDialog(Context context, CustomLIstener customLIstener) {
        super(context);
        setContentView(R.layout.custom_dialog_update);
        this.mCustomLIstener = customLIstener;

        mTextViewOk = findViewById(R.id.dialog_update_ok);
        mTextViewOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCustomLIstener.goUpdateClick();
                dismiss();
            }
        });

        mTextViewCancel = findViewById(R.id.dialog_update_cancel);
        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
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
