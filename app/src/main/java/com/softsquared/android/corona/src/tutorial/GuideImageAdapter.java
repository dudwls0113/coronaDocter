package com.softsquared.android.corona.src.tutorial;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class GuideImageAdapter extends FragmentStatePagerAdapter {

//    private ArrayList<Integer> imageUrl = new ArrayList<>();
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public GuideImageAdapter(FragmentManager fm) {
        super(fm);
//        imageUrl = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
//        if(imageUrl !=null && imageUrl.size()>0){
//            position = position%imageUrl.size();
//            return GuideImageFragment.newInstance(imageUrl.get(position));
//        }
//        else{
//            return GuideImageFragment.newInstance(0);
//        }
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addItem(Fragment fragment){fragments.add(fragment);}
}
