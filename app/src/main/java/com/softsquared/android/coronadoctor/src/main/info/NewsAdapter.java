package com.softsquared.android.coronadoctor.src.main.info;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private ArrayList<CaringInfo> arrayList;

    public static int LOOPS_COUNT = 1000;

    private List<Fragment> mFragments = new ArrayList<>();

    public NewsAdapter(FragmentManager fm, ArrayList<CaringInfo> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Fragment getItem(int i) {
        if(arrayList !=null && arrayList.size()>0){
            i = i%arrayList.size();
            if(arrayList.get(i).getType() == 4){
                return BannerFragment.newInstance(arrayList.get(i), arrayList.get(i).getArrayListName());
            }
            else{
                return BannerFragment.newInstance(arrayList.get(i));
            }
        }
        else{
            return BannerFragment.newInstance(null);
        }
    }
}
