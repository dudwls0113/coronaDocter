package com.softsquared.android.corona.src.main.news;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Objects;


public class NewsFragment extends BaseFragment  {

    Context mContext;
    WebView mWebView;
    FloatingActionButton mFlatingBtn;
    public static String URL = "https://news.google.com/search?q=%EC%BD%94%EB%A1%9C%EB%82%98&hl=ko&gl=KR&ceid=KR%3Ako";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mWebView = v.findViewById(R.id.fragment_news_web_view);
        mFlatingBtn = v.findViewById(R.id.floatingActionButton);

        WebSettings mws = mWebView.getSettings();//Mobile Web Setting
        mws.setJavaScriptEnabled(true);//자바스크립트 허용
        mws.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        mWebView.setWebViewClient(new WebViewClient() {
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

        mWebView.loadUrl(URL);
    }

    public int webViewBack(){
        if(mWebView.canGoBack()){
//            Log.d("웹뷰 뒤로가기", " ");
            mWebView.goBack();
            return 1;
        }
        else{
            return 0;
        }
    }

}
