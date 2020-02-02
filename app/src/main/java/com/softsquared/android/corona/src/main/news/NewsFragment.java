package com.softsquared.android.corona.src.main.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.naver.maps.map.MapView;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class NewsFragment extends BaseFragment {

    Context mContext;
    private MapView mapView;
    WebView mWebView;
    FloatingActionButton mFlatingBtn;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        v.setFocusableInTouchMode(true);
        v.requestFocus();
        v.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                        return false;
                    }
                    else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                }
                return true;
            }
        });



        mWebView=v.findViewById(R.id.fragment_news_web_view);
        mFlatingBtn=v.findViewById(R.id.floatingActionButton);

        WebSettings mws=mWebView.getSettings();//Mobile Web Setting
        mws.setJavaScriptEnabled(true);//자바스크립트 허용
        mws.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


        mFlatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KakaoShareCustomDialog kakaoShareCustomDialog = new KakaoShareCustomDialog(getContext(), mWebView.getUrl());
                kakaoShareCustomDialog.show();
                Snackbar.make(view, mWebView.getUrl(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
//        Intent intent = getActivity().getIntent();
//        mKeyword = intent.getExtras().getString("keyword");
//        try {
//            mKeyword = URLEncoder.encode(mKeyword, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        String url = "https://news.google.com/search?q=%EC%BD%94%EB%A1%9C%EB%82%98&hl=ko&gl=KR&ceid=KR%3Ako";
        mWebView.loadUrl(url);
    }

    public void myOnKeyDown(){
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }
    }
}
