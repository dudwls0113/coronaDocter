package com.softsquared.android.corona.src.main.community;

import com.softsquared.android.corona.src.main.community.interfaces.CommunityRetrofitInterface;
import com.softsquared.android.corona.src.main.community.interfaces.CommunityView;
import com.softsquared.android.corona.src.main.community.interfaces.PostDetailView;
import com.softsquared.android.corona.src.main.community.model.PostDetailResponse;
import com.softsquared.android.corona.src.main.community.model.PostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;

public class CommunityService {
    private final CommunityView mCommunityView;

    public CommunityService(CommunityView communityView){
        mCommunityView = communityView;
    }

    void getHotPost(){
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.getHotPost().enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                final PostResponse postResponse = response.body();
                if(postResponse==null){
                    mCommunityView.validateFailure(null);
                }
                else if(postResponse.getCode()==100){
                    mCommunityView.getHotPostSuccess(postResponse.getmPosts());
                }
                else{
                    mCommunityView.validateFailure(postResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }

    void getNewPost(int page){
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.getNewPost(page).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                final PostResponse postResponse = response.body();
                if(postResponse==null){
                    mCommunityView.validateFailure(null);
                }
                else if(postResponse.getCode()==100){
                    mCommunityView.getNewPostSuccess(postResponse.getmPosts());
                }
                else{
                    mCommunityView.validateFailure(postResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }

}
