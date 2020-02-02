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
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;
import com.softsquared.android.corona.src.main.info.interfaces.InfoFragmentView;
import com.softsquared.android.corona.src.main.info.models.Info;

import java.util.ArrayList;


public class InfoFragment extends BaseFragment implements InfoFragmentView {

    ViewPager mViewPager;
    NewsAdapter newsAdapter;
    TabLayout mTabLayout;

    private TextView mTextViewInfected, mTextViewDead, mTextViewCured, mTextViewSymptom,
            mTextViewInspection, mTextViewIsolation, mTextViewInfectedDiff, mTextViewDeadDiff,
            mTextViewCuredDiff, mTextViewSymptomDiff, mTextViewInspectionDiff,
            mTextViewIsolationDiff, mTextViewUpdate;


    ArrayList<CaringInfo> mArrayListCaringInfos = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_info, container, false);
        setComponentView(view);
        getInfo();
        return view;
    }

    @Override
    public void setComponentView(View v) {
        mTabLayout = v.findViewById(R.id.home_tab_indicator);
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

        mTextViewCured = v.findViewById(R.id.info_cure);
        mTextViewCuredDiff = v.findViewById(R.id.info_cure_plus);
        mTextViewDead = v.findViewById(R.id.info_death);
        mTextViewDeadDiff = v.findViewById(R.id.info_death_plus);
        mTextViewInfected = v.findViewById(R.id.info_infected);
        mTextViewInfectedDiff = v.findViewById(R.id.info_infected_plus);
        mTextViewInspection = v.findViewById(R.id.info_inspection);
        mTextViewInspectionDiff = v.findViewById(R.id.info_inspection_plus);
        mTextViewIsolation = v.findViewById(R.id.info_isolation);
        mTextViewIsolationDiff = v.findViewById(R.id.info_isolation_plus);
        mTextViewSymptom = v.findViewById(R.id.info_symptom);
        mTextViewSymptomDiff = v.findViewById(R.id.info_symptom_plus);
        mTextViewUpdate = v.findViewById(R.id.info_update);
    }

    void getInfo(){
        showProgressDialog(getActivity());
        final InfoFragmentService infoFragmentService = new InfoFragmentService(this);
        infoFragmentService.getStatistics();
    }

    @Override
    public void validateSuccess(Info info) {
        hideProgressDialog();
        mTextViewUpdate.setText(info.getUpdatedTime());
        mTextViewCured.setText("( "+info.getCured()+" )");
        if(info.getCuredDiff()>0){
            mTextViewCuredDiff.setText("( +"+info.getCuredDiff()+" )");
        }
        else{
            mTextViewCuredDiff.setText("( "+info.getCuredDiff()+" )");
        }
        mTextViewDead.setText("( "+info.getDead()+" )");
        if(info.getDeadDiff()>0){
            mTextViewDeadDiff.setText("( +"+info.getDeadDiff()+" )");
        }
        else{
            mTextViewDeadDiff.setText("( "+info.getDeadDiff()+" )");
        }
        mTextViewInfected.setText("( "+info.getInfected()+" )");
        if (info.getInfectedDiff()>0){
            mTextViewInfectedDiff.setText("( +"+info.getInfectedDiff()+" )");
        }
        else{
            mTextViewInfectedDiff.setText("( "+info.getInfectedDiff()+" )");
        }
        mTextViewInspection.setText("( "+info.getInspection()+" )");
        if(info.getInspectionDiff()>0){
            mTextViewInspectionDiff.setText("( "+info.getInspectionDiff()+" )");
        }
        else{
            mTextViewInspectionDiff.setText("( "+info.getInspectionDiff()+" )");
        }
        mTextViewIsolation.setText("( "+info.getIsolation()+" )");
        if(info.getIsolationDiff()>0){
            mTextViewIsolationDiff.setText("( +"+info.getIsolationDiff()+")");
        }
        else{
            mTextViewIsolationDiff.setText("( "+info.getIsolationDiff()+")");
        }
        mTextViewSymptom.setText("( "+info.getSymptom()+" )");
        if (info.getSymptomDiff()>0){
            mTextViewSymptomDiff.setText("( +"+info.getSymptomDiff()+" )");
        }
        else{
            mTextViewSymptomDiff.setText("( "+info.getSymptomDiff()+" )");
        }
    }

    @Override
    public void validateFailure(String message) {
        hideProgressDialog();
        showCustomToast(message == null || message.isEmpty() ? getString(R.string.network_error) : message);
    }
}
