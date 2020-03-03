package com.softsquared.android.corona.src.tutorial;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.softsquared.android.corona.R;


public class GuideImageFragment extends Fragment {

//    int imageId;
//
//    public static Fragment newInstance(int imageUrl){
//        GuideImageFragment imageFragment = new GuideImageFragment();
//        Bundle args = new Bundle();
//        args.putInt("imageUrl", imageUrl);
//        imageFragment.setArguments(args);
//        return imageFragment;
//    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            Bundle args = getArguments();
//            System.out.println("실행");
//            imageId = args.getInt("imgUrl");
////            Glide.with(this).load(args.getInt("imgUrl")).apply(new RequestOptions().centerCrop()).into(imageView);
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide_image, container, false);

        ImageView imageView = view.findViewById(R.id.iv_guide);
        if (getArguments() != null) {
            Bundle args = getArguments();
            imageView.setImageResource(args.getInt("imgUrl"));
//            Glide.with(this).load(args.getInt("imgUrl")).apply(new RequestOptions().centerCrop()).into(imageView);
        }
//        System.out.println("imageId: " + imageId);
//        if (imageId==1){
//            imageView.setImageResource(R.drawable.img_splash2);
//        }
//        else if(imageId==2){
//            imageView.setImageResource(R.drawable.img_splash3);
//        }
//        else if (imageId==3){
//            imageView.setImageResource(R.drawable.img_splash4);
//        }
//        else if(imageId==4){
//            imageView.setImageResource(R.drawable.img_splash5);
//        }
        return view;
    }



}
