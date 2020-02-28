package com.softsquared.android.coronadoctor.src.main.community;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.softsquared.android.coronadoctor.R;
import com.softsquared.android.coronadoctor.src.main.news.KakaoShareCustomDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class OrderWebViewActivity extends AppCompatActivity {
    Context mContext;
    WebView mWebView;
    FloatingActionButton mFlatingBtn;
    String mKeyword;
    ImageView mImageViewBack;

    String url = "https://news.google.com/search?q=%EC%BD%94%EB%A1%9C%EB%82%98&hl=ko&gl=KR&ceid=KR%3Ako";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_web_view);
        mContext = this;

        mWebView = findViewById(R.id.activity_order_web_view);
        mFlatingBtn = findViewById(R.id.floatingActionButton);
        mImageViewBack = findViewById(R.id.activity_order_btn_back);
        WebSettings mws = mWebView.getSettings();//Mobile Web Setting
        mws.setJavaScriptEnabled(true);//자바스크립트 허용
        mws.setLoadWithOverviewMode(true);//컨텐츠가 웹뷰보다 클 경우 스크린 크기에 맞게 조정

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                KakaoShareCustomDialog kakaoShareCustomDialog = new KakaoShareCustomDialog(mContext, mWebView.getUrl());
                kakaoShareCustomDialog.show();
//                Snackbar.make(view, mWebView.getUrl(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });

        Intent intent = getIntent();
        mKeyword = intent.getExtras().getString("keyword");
        try {
            mKeyword = URLEncoder.encode(mKeyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String url = "https://msearch.shopping.naver.com/search/all.nhn?query=" + mKeyword + "&cat_id=&frm=NVSHATC";

        mWebView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
