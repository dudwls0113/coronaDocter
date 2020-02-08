package com.softsquared.android.corona.src.main.community.interfaces;

import com.softsquared.android.corona.src.main.community.model.PostDetailResponse;
import com.softsquared.android.corona.src.main.community.model.PostResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CommunityRetrofitInterface {
    @GET("/corona/hotPost")
    Call<PostResponse> getHotPost();

    @GET("/corona/post/{page}")
    Call<PostResponse> getNewPost(@Path("page") final int page);

    @GET("/corona/postDetail/{postNo}")
    Call<PostDetailResponse> getPostDetail(@Path("postNo") final int postNo);
}
