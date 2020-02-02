package com.softsquared.android.corona.src.main.info;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseFragment;


public class InfoFragment extends BaseFragment {

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

    }
}
