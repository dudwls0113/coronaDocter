package com.softsquared.android.corona.src.main.info;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.news.KakaoShareCustomDialog;


public class BannerFragment extends BaseFragment {

    ImageView imageView;
    Context mContext;
    int imageId;
    int type;

    public static Fragment newInstance(CaringInfo caringInfo){
        BannerFragment bannerFragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putInt("imageId", caringInfo.getCaringId());
        args.putInt("type",caringInfo.getType());
        bannerFragment.setArguments(args);
        return bannerFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            imageId = getArguments().getInt("imageId");
            type = getArguments().getInt("type");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        mContext=getContext();
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        imageView = v.findViewById(R.id.news_iv_banner);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(type == 1){//카톡 공유
                    KakaoShareCustomDialog kakaoShareCustomDialog = new KakaoShareCustomDialog(getContext(), "https://play.google.com/store/apps/details?id=com.softsquared.android.corona");
                    kakaoShareCustomDialog.show();
//                    Snackbar.make(view, mWebView.getUrl(), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                }
                else{

                }
            }
        });
        GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.bg_round_trans_trans);
        imageView.setBackground(drawable);
        imageView.setClipToOutline(true);
        imageView.setImageResource(imageId);
    }
}
