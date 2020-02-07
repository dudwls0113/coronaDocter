package com.softsquared.android.corona.src.main.community;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.info.CaringInfo;
import com.softsquared.android.corona.src.main.info.NewsAdapter;

import java.util.ArrayList;


public class CommunityFragment extends BaseFragment {

    ViewPager mViewPager;
    NewsAdapter newsAdapter;
    TabLayout mTabLayout;
    Button mBtnCancel;
    ImageView mImageViewMask, mImageViewHand;
    LinearLayout mLinearHeader, mLinearContent;


    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();
    boolean mIsExpanded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_community, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mTabLayout = v.findViewById(R.id.home_tab_indicator);
        mLinearHeader = v.findViewById(R.id.fragment_community_linear_header);
        mLinearContent = v.findViewById(R.id.fragment_community_linear_content);
        mBtnCancel = v.findViewById(R.id.dialog_infect_select_btn_cancel);

        mArrayListCaringInfos.clear();
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_invite, "", 1));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_tip, "", 2));

        newsAdapter = new NewsAdapter(getChildFragmentManager(), mArrayListCaringInfos);

        mViewPager = v.findViewById(R.id.fragment_info_vp);
//       뷰페이저 미리보기 설정//
        mViewPager.setClipToPadding(false);
//        int dpValue = 25;
//        float d = getResources().getDisplayMetrics().density;
//        int margin = (int) (dpValue * d);
//        mViewPager.setPadding(margin/2, 0, margin, 0);
//        mViewPager.setPageMargin(margin/3);
        mViewPager.setAdapter(newsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

//        mImageViewMask.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "KF94마스크");
//                startActivity(intent3);
//            }
//        });
//        mImageViewHand.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent3 = new Intent(getContext(), OrderWebViewActivity.class);
//                intent3.putExtra("keyword", "손세정제");
//                startActivity(intent3);
//            }
//        });
        mLinearHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWrite();
                if (mIsExpanded) {
                    mIsExpanded = false;
                } else {
                    mIsExpanded = true;
                }
            }
        });
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideWrite();
            }
        });
    }

    private void showWrite() {
        ValueAnimator anim2 = ValueAnimator.ofInt(0,700);
        anim2.setDuration(500); // duration 5 seconds
//        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
//        mLinearHeader.setVisibility(View.GONE);
    }


    private void hideWrite() {
        ValueAnimator anim2 = ValueAnimator.ofInt(800,0);
        anim2.setDuration(500); // duration 5 seconds
//        anim2.setRepeatCount(ValueAnimator.INFINITE);
        anim2.setRepeatMode(ValueAnimator.REVERSE);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mLinearContent.getLayoutParams().height = value.intValue();
                mLinearContent.requestLayout();
            }
        });
        anim2.start();
//        mLinearHeader.setVisibility(View.VISIBLE);
    }
}
