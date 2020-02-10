package com.softsquared.android.corona.src.tutorial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.main.MainActivity;

import java.util.ArrayList;

public class TutorialActivity extends BaseActivity {

    private ViewPager mViewPagerImage;
    private TextView mTextViewSkip, mTextViewStart;
    private Context mContext;
    private TabLayout mTabLayout;
    private GuideImageAdapter guideImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        mContext = this;
        init();
    }

    void init(){
        mViewPagerImage = findViewById(R.id.tutorial_vp);
        mTextViewSkip = findViewById(R.id.tutorial_skip);
        mTextViewSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mTextViewStart = findViewById(R.id.main_start);
        mTextViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mTabLayout = findViewById(R.id.tutorial_tab);
//        ArrayList<Integer> imageUrl = new ArrayList<>();
//        imageUrl.add(1);
//        imageUrl.add(2);
//        imageUrl.add(3);
//        imageUrl.add(4);
        guideImageAdapter = new GuideImageAdapter(getSupportFragmentManager());
        mTabLayout.setupWithViewPager(mViewPagerImage,true);
        mViewPagerImage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==3){
                    mTextViewStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPagerImage.setAdapter(guideImageAdapter);

        for (int i = 0; i < 4; i++) {
            GuideImageFragment imageFragment = new GuideImageFragment();
            Bundle bundle = new Bundle();
            if(i==0){
                bundle.putInt("imgUrl", R.drawable.img_splash2);
            }
            else if(i==1){
                bundle.putInt("imgUrl", R.drawable.img_splash3);
            }
            else if(i==2){
                bundle.putInt("imgUrl", R.drawable.img_splash4);
            }
            else{
                bundle.putInt("imgUrl", R.drawable.img_splash5);
            }
            imageFragment.setArguments(bundle);
            guideImageAdapter.addItem(imageFragment);
        }
        guideImageAdapter.notifyDataSetChanged();
    }
}
