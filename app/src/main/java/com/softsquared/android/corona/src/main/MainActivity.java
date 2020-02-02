package com.softsquared.android.corona.src.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.info.InfoFragment;
import com.softsquared.android.corona.src.main.interfaces.MainActivityView;
import com.softsquared.android.corona.src.main.map.MapViewFragment;
import com.softsquared.android.corona.src.main.news.NewsFragment;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends BaseActivity implements MainActivityView {
    private TextView mTextViewTitle;
    public static int IS_WEBVIEW_MODE = 0;

    final int MAP_FRAGMENT = 0;
    final int NEWS_FRAGMENT = 1;
    final int ORDER_FRAGMENT = 2;
    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;
    final int INFO_FRAGMENT = 3;

    public static MainViewPager mViewPagerMain;
    BaseFragment mDealingFragments;
    NewsFragment mNewsFragments;
    BaseFragment mOrderFragment;
    BaseFragment mInfoFragment;
    MainFragmentPagerAdapter mMainFragmentPagerAdapter;

    private RelativeLayout mRelativeTopBar;

    public static NavigationTabBar mNavigationTabBar;
    private ArrayList<NavigationTabBar.Model> mNavigationTabBarModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mDealingFragments = new MapViewFragment();
        mNewsFragments = new NewsFragment();
        mOrderFragment = new NewsFragment();
        mInfoFragment = new InfoFragment();

        mTextViewTitle = findViewById(R.id.activity_main_tv);
        mRelativeTopBar = findViewById(R.id.activity_main_relative_top);
//        mTextViewTitle = findViewById(R.id.activity_main_tv);
//        mImageViewTitle = findViewById(R.id.activity_main_iv_title);

        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mMainFragmentPagerAdapter.addFragment(mDealingFragments, "1");
        mMainFragmentPagerAdapter.addFragment(mNewsFragments, "2");
        mMainFragmentPagerAdapter.addFragment(mOrderFragment, "3");
        mMainFragmentPagerAdapter.addFragment(mInfoFragment, "4");

        mViewPagerMain = findViewById(R.id.vpMainViewPager);
        mViewPagerMain.setAdapter(mMainFragmentPagerAdapter);
        mViewPagerMain.setPagingEnabled(false);

        mNavigationTabBar = findViewById(R.id.ntb);
        mNavigationTabBarModels = new ArrayList<>();
        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_off_maps2),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("지도")
                        .build()
        );


        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_news_off2),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("뉴스")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_order_off2),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("구매")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_info_off2),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("감염 정보")
                        .build()
        );

        mNavigationTabBar.setModels(mNavigationTabBarModels);
        mNavigationTabBar.setIsBadged(true);
        mNavigationTabBar.setViewPager(mViewPagerMain, 0); // 처음으로 0번째 Fragment 를 보여줍니다

        mViewPagerMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case MAP_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.GONE);
                        mTextViewTitle.setText("지도");
                        IS_WEBVIEW_MODE = 0;
                        break;

                    case NEWS_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("뉴스");
                        IS_WEBVIEW_MODE = 1;
                        break;

                    case ORDER_FRAGMENT:
                        IS_WEBVIEW_MODE = 0;

                        break;
                    case INFO_FRAGMENT:
                        IS_WEBVIEW_MODE = 0;

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mNavigationTabBar.setBadgeTitleColor(Color.WHITE);
        mNavigationTabBar.setIsSwiped(true);
    }


    private void tryGetTest() {
        showProgressDialog();

        final MainService mainService = new MainService(this);
        mainService.getTest();
    }

    @Override
    public void validateSuccess(String text) {
        hideProgressDialog();
        mTextViewTitle.setText(text);
    }

    @Override
    public void validateFailure(@Nullable String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }

    // 뒤로가기 버튼을 눌렀을 때의 오버라이드 메소드
    @Override
    public void onBackPressed() {
//        Log.d("뒤로가기", " ");

        if (IS_WEBVIEW_MODE == 1) {
            try {
                int backResult = mNewsFragments.webViewBack();
                if (backResult == 1) { //웹뷰 뒤로가기 상공
                } else {//뒤로갈곳 없음
                    closeApp();
                }
            } catch (Exception e) {
                //back-key code of another fragment
            }
        } else {
//            Log.d("앱 뒤로가기", " ");
            closeApp();
        }
    }

    void closeApp() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Snackbar.make(findViewById(R.id.activity_main_tv),
                    " 뒤로가기를 한번 더 누르면 앱을 종료합니다", Snackbar.LENGTH_LONG).show();
        }
    }


    public void customOnClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_hello_world:
                tryGetTest();
                break;
            default:
                break;
        }
    }
}
