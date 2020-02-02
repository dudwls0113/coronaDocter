package com.softsquared.android.corona.src.main.news;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

public class KakaoShareCustomDialog extends Dialog {

    TextView mTextViewCancel, mTextViewOpen;
    Context mContext;
    String mImgUrl, mValue, mBrand, mName, mCategory, mSubCategory, mShareUrl = null;
    long mProductId;

    public KakaoShareCustomDialog(Context context, String shareUrl) {
        super(context);
        mContext = context;
        mShareUrl = shareUrl;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.custom_dialog_kakao_share);     //다이얼로그에서 사용할 레이아웃입니다.

        mTextViewCancel = findViewById(R.id.dialog_kakao_tv_cancle);
        mTextViewOpen = findViewById(R.id.dialog_kakao_tv_open);
        mTextViewCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();   //다이얼로그를 닫는 메소드
            }
        });
        mTextViewOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent kakaoIntent = new Intent();
                kakaoIntent.setAction(Intent.ACTION_SEND);
                kakaoIntent.setType("text/plain");
                kakaoIntent.setPackage("com.kakao.talk");
                kakaoIntent.putExtra(Intent.EXTRA_SUBJECT, "[베럽 이벤트 알림]\n");
                kakaoIntent.putExtra(Intent.EXTRA_TEXT, mShareUrl);
                try {
                    mContext.startActivity(kakaoIntent);
//                    shareKakaoLink();
                } catch (Exception e) {
                    ((MainActivity) mContext).showCustomToast("카카오톡 앱이 설치 되어있지 않습니다.");
                    // 카톡어플이 없다는 팝업
                }
                dismiss();
            }
        });
    }


}
