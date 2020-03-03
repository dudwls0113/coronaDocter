package com.softsquared.android.corona.src.main.community.interfaces;

import com.softsquared.android.corona.src.main.community.model.Comment;
import com.softsquared.android.corona.src.main.community.model.Post;
import com.softsquared.android.corona.src.main.community.model.Sponsor;

import java.util.ArrayList;

public interface CommunityView {
    void getHotPostSuccess(ArrayList<Post> arrayList);

    void getNewPostSuccess(ArrayList<Post> arrayList);

    void getSponsorSuccess(ArrayList<Sponsor> arrayList);

    void postLikeSuccess(int mode, int position);

    void postWritePostSuccess();

    void validateFailure(String message);

    void postReportSuccess();
}
