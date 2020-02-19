package com.softsquared.android.corona.src.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.snackbar.Snackbar;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.community.PostDetailActivity;
import com.softsquared.android.corona.src.main.faq.FaqFragment;
import com.softsquared.android.corona.src.main.info.InfoFragment;
import com.softsquared.android.corona.src.main.interfaces.MainActivityView;
import com.softsquared.android.corona.src.main.map.MapViewFragment;
import com.softsquared.android.corona.src.main.news.NewsFragment;
import com.softsquared.android.corona.src.main.community.CommunityFragment;

import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

import static com.softsquared.android.corona.src.ApplicationClass.PUSH_ON_OFF;
import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;

public class MainActivity extends BaseActivity implements MainActivityView {
    private TextView mTextViewTitle;
    public static int IS_WEBVIEW_MODE = 0;

    final int MAP_FRAGMENT = 0;
    final int COMMUNITY_FRAGMENT = 1;
    final int NEWS_FRAGMENT = 3;
    final int INFO_FRAGMENT = 2;
    final int FAQ_FRAGMENT = 4;

    private long backPressedTime = 0;
    private final long FINISH_INTERVAL_TIME = 2000;

    public static MainViewPager mViewPagerMain;
    BaseFragment mMapFragments;
    NewsFragment mNewsFragments;
    BaseFragment mCommunityFragment;
    BaseFragment mInfoFragment;
    BaseFragment mFaqFragment;
    MainFragmentPagerAdapter mMainFragmentPagerAdapter;

    private RelativeLayout mRelativeTopBar;
    private LinearLayout mLinearNoti;
    private ImageView mImageViewNoti;


    public static NavigationTabBar mNavigationTabBar;
    private ArrayList<NavigationTabBar.Model> mNavigationTabBarModels;
    private InterstitialAd mInterstitialAd;

    private AdView mAdView;
    private LinearLayout mLinear;

    public static final String AD_TEST_KEY_BANNER = "ca-app-pub-3940256099942544/6300978111";
    public static final String AD_TEST_KEY_INTERESTITIAL = "ca-app-pub-3940256099942544/1033173712";

//      [주의] 실제키로 빌드하면안됨
//    public static final String AD_REAL_KEY_BANNER = "ca-app-pub-2165488373168832/8270923170";
//    public static final String AD_REAL_KEY_INTERESTITIAL = "ca-app-pub-2165488373168832/3881904411";
//      [주의] 실제키로 빌드하면안됨

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(AD_TEST_KEY_INTERESTITIAL);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        initView();
        moveToCommunityTab();

        mAdView = new AdView(this);
        mAdView.setAdUnitId(AD_TEST_KEY_BANNER);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.

            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });


        mLinear = findViewById(R.id.activity_main_linear);
        // Step 1 - Create AdView and set the ad unit ID on it.
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.gravity = Gravity.TOP;ㅌ
        mLinear.addView(mAdView, params);
        loadBanner();
    }

    private void moveToCommunityTab() {
        int postNo = getIntent().getIntExtra("postNo", 0);
        if (getIntent().getBooleanExtra("moveCommunityTab", false)) {
            mNavigationTabBar.setViewPager(mViewPagerMain, 1);
            Log.d("로그1111", postNo + "");
        } else if (postNo > 0) {
            mNavigationTabBar.setViewPager(mViewPagerMain, 1);
            Log.d("로그", postNo + "");
            Intent intent = new Intent(this, PostDetailActivity.class);
            intent.putExtra("postNo", postNo);
            startActivity(intent);

        }
    }

    private void initView() {
        mMapFragments = new MapViewFragment();
        mCommunityFragment = new CommunityFragment();
        mInfoFragment = new InfoFragment();
        mNewsFragments = new NewsFragment();
        mFaqFragment = new FaqFragment();

        mTextViewTitle = findViewById(R.id.activity_main_tv);
        mRelativeTopBar = findViewById(R.id.activity_main_relative_top);
        mLinearNoti = findViewById(R.id.activity_main_linear_noti);
        mImageViewNoti = findViewById(R.id.activity_main_iv_noti);
//        mTextViewTitle = findViewById(R.id.activity_main_tv);
//        mImageViewTitle = findViewById(R.id.activity_main_iv_title);

        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());
        mMainFragmentPagerAdapter.addFragment(mMapFragments, "1");
        mMainFragmentPagerAdapter.addFragment(mCommunityFragment, "2");
        mMainFragmentPagerAdapter.addFragment(mInfoFragment, "3");
        mMainFragmentPagerAdapter.addFragment(mNewsFragments, "4");
        mMainFragmentPagerAdapter.addFragment(mFaqFragment, "5");

        mViewPagerMain = findViewById(R.id.vpMainViewPager);
        mViewPagerMain.setAdapter(mMainFragmentPagerAdapter);
        mViewPagerMain.setPagingEnabled(false);

        mNavigationTabBar = findViewById(R.id.ntb);
        mNavigationTabBarModels = new ArrayList<>();
        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_map),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_community),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_info),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_news),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
                        .build()
        );

        mNavigationTabBarModels.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_tab_faq),
                        getResources().getColor(R.color.colorWhite)
                )
                        .title("")
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
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);
                        mTextViewTitle.setText("지도");
                        IS_WEBVIEW_MODE = 0;
                        break;

                    case NEWS_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);
                        mTextViewTitle.setText("뉴스");
                        IS_WEBVIEW_MODE = 1;
                        break;

                    case COMMUNITY_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.VISIBLE);
                        mTextViewTitle.setText("커뮤니티");
                        IS_WEBVIEW_MODE = 0;

                        break;
                    case INFO_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.VISIBLE);

                        mTextViewTitle.setText("감염 정보");
                        IS_WEBVIEW_MODE = 0;
                        break;
                    case FAQ_FRAGMENT:
                        mRelativeTopBar.setVisibility(View.VISIBLE);
                        mTextViewTitle.setVisibility(View.VISIBLE);
                        mLinearNoti.setVisibility(View.GONE);

                        mTextViewTitle.setText("질문 답변");
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

        setNotiIcon();
        mImageViewNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences spf = sSharedPreferences;
                boolean isPushOn = spf.getBoolean(PUSH_ON_OFF, true);
                final SharedPreferences.Editor editor = sSharedPreferences.edit();
                if (isPushOn) {
                    editor.putBoolean(PUSH_ON_OFF, false);
                } else {
                    editor.putBoolean(PUSH_ON_OFF, true);
                }
                editor.apply();
                setNotiIcon();
            }
        });
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
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
                Log.d("TAG", "The interstitial wasn't loaded yet.");
            }
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Snackbar.make(findViewById(R.id.activity_main_tv),
                    " 뒤로가기를 한번 더 누르면 앱을 종료합니다", Snackbar.LENGTH_LONG).show();
        }
    }

    public void setNotiIcon() {
        SharedPreferences spf = sSharedPreferences;
        boolean isPushOn = spf.getBoolean(PUSH_ON_OFF, true);
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        if (isPushOn) {
            mImageViewNoti.setImageResource(R.drawable.img_noti_on);
            editor.putBoolean(PUSH_ON_OFF, false);
        } else {
            mImageViewNoti.setImageResource(R.drawable.img_noti_off);
            editor.putBoolean(PUSH_ON_OFF, true);
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

    private void loadBanner() {
        // Create an ad request. Check your logcat output for the hashed device ID
        // to get test ads on a physical device, e.g.,
        // "Use AdRequest.Builder.addTestDevice("ABCDE0123") to get test ads on this
        // device."
        AdRequest adRequest =
                new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                        .build();

        AdSize adSize = getAdSize();
        // Step 4 - Set the adaptive ad size on the ad view.
        mAdView.setAdSize(adSize);

        // Step 5 - Start loading the ad in the background.
        mAdView.loadAd(adRequest);
    }


    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }
}
