package com.softsquared.android.corona.src.main.faq;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;



public class FaqFragment extends BaseFragment {

    Context mContext;
    WebView mWebView;
    private static String URL2 = "http://www.cdc.go.kr/linkCheck.es?mid=a21111050400";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        setComponentView(view);
        return view;
    }


    @Override
    public void setComponentView(View v) {
        mWebView = v.findViewById(R.id.fragment_faq_web_view);

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

        mWebView.loadUrl(URL2);
    }
}
