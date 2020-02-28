package com.softsquared.android.coronadoctor.src.main.info;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.BaseFragment;
import com.softsquared.android.coronadoctor.src.CustomMoneyDialog;
import com.softsquared.android.coronadoctor.src.main.news.KakaoShareCustomDialog;
import com.softsquared.android.coronadoctor.src.main.community.OrderWebViewActivity;

import java.util.ArrayList;


public class BannerFragment extends BaseFragment {

    ImageView imageView;
    Context mContext;
    int imageId;
    int type;
    String title;
    ArrayList<String> stringArrayList;

    public static Fragment newInstance(CaringInfo caringInfo) {
        BannerFragment bannerFragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putInt("imageId", caringInfo.getCaringId());
        args.putInt("type", caringInfo.getType());
        args.putString("title", caringInfo.getTitle());
        bannerFragment.setArguments(args);
        return bannerFragment;
    }

    public static Fragment newInstance(CaringInfo caringInfo, ArrayList<String> peopleList) {
        BannerFragment bannerFragment = new BannerFragment();
        Bundle args = new Bundle();
        args.putInt("imageId", caringInfo.getCaringId());
        args.putInt("type", caringInfo.getType());
        args.putString("title", caringInfo.getTitle());
        args.putStringArrayList("peopleList", peopleList);
        bannerFragment.setArguments(args);
        return bannerFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            imageId = getArguments().getInt("imageId");
            type = getArguments().getInt("type");
            title = getArguments().getString("title");
            if (getArguments().getStringArrayList("peopleList") != null) {
                stringArrayList = getArguments().getStringArrayList("peopleList");
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_banner, container, false);
        mContext = getContext();
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        imageView = v.findViewById(R.id.news_iv_banner);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {//카톡 공유
                    KakaoShareCustomDialog kakaoShareCustomDialog = new KakaoShareCustomDialog(getContext(), "https://play.google.com/store/apps/details?id=com.softsquared.android.corona");
                    kakaoShareCustomDialog.show();
//                    Snackbar.make(view, mWebView.getUrl(), Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
                } else if (type == 2) {//다이얼로그
                    BannerLongDialog bannerLongDialog = new BannerLongDialog(getContext());
                    bannerLongDialog.show();
                } else if (type == 4) {//구매
                    CustomMoneyDialog customMoneyDialog = new CustomMoneyDialog(mContext, stringArrayList);
                    customMoneyDialog.show();
                } else if(type == 5) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);

                    try {
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"coronadoctora@gmail.com"});

                        emailIntent.setType("text/html");
                        emailIntent.setPackage("com.google.android.gm");
                        if (emailIntent.resolveActivity(mContext.getPackageManager()) != null)
                            startActivity(emailIntent);

                        startActivity(emailIntent);
                    } catch (Exception e) {
                        e.printStackTrace();

                        emailIntent.setType("text/html");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"coronadoctora@gmail.com"});

                        startActivity(Intent.createChooser(emailIntent, "Send Email"));
                    }
                } else {
                    Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
                    intent3.putExtra("keyword", title);
                    startActivity(intent3);
                }
            }
        });
        GradientDrawable drawable = (GradientDrawable) mContext.getDrawable(R.drawable.bg_round_trans_trans);
        imageView.setBackground(drawable);
        imageView.setClipToOutline(true);
        imageView.setImageResource(imageId);
    }
}
