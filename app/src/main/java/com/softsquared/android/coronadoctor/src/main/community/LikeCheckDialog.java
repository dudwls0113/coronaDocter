package com.softsquared.android.coronadoctor.src.main.community;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.softsquared.android.coronadoctor.R;


public class LikeCheckDialog extends Dialog {

    private Button mBtnGoUpdate;
    private TextView mBtnCancle;
    private View mView;

    private CustomLIstener mCustomLIstener;
    Context mContext;

    public LikeCheckDialog(Context context, CustomLIstener customLIstener) {
        super(context);
        mContext = context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.custom_dialog_like_check);


//        GradientDrawable drawable = (GradientDrawable) context.getDrawable(R.drawable.bg_round);
//        mView.setBackground(drawable);
//        mView.setClipToOutline(true);

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
