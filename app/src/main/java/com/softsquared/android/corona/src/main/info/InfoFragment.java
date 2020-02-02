package com.softsquared.android.corona.src.main.info;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ExpandableListView;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;

import java.util.ArrayList;


public class InfoFragment extends BaseFragment {

    ViewPager mViewPager;
    NewsAdapter newsAdapter;
    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.banner_1, "", 1));
        mArrayListCaringInfos.add(new CaringInfo(R.drawable.bg_round, "", 1));

        newsAdapter = new NewsAdapter(getChildFragmentManager(), mArrayListCaringInfos);

        mViewPager = v.findViewById(R.id.fragment_info_vp);
//       뷰페이저 미리보기 설정//
        mViewPager.setClipToPadding(false);
        int dpValue = 30;
        float d = getResources().getDisplayMetrics().density;
        int margin = (int) (dpValue * d);
        mViewPager.setPadding(margin/2, 0, margin, 0);
        mViewPager.setPageMargin(margin/3);
        mViewPager.setAdapter(newsAdapter);

    }
}
