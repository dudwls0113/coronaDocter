package com.softsquared.android.corona.src.main.news;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.naver.maps.map.MapView;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;


public class NewsFragment extends BaseFragment {

    Context mContext;
    private MapView mapView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        setComponentView(view);
        return view;
    }

    @Override
    public void setComponentView(View v) {

    }
}
