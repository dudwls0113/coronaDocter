package com.softsquared.android.corona.src.main.community.interfaces;

import com.softsquared.android.corona.src.main.community.model.Post;

import java.util.ArrayList;

public interface CommunityView {
    void getHotPostSuccess(ArrayList<Post> arrayList);

    void getNewPostSuccess(ArrayList<Post> arrayList);

    void validateFailure(String message);
}
