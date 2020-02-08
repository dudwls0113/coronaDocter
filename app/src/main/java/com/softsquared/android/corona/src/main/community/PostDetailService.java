package com.softsquared.android.corona.src.main.community;

import com.softsquared.android.corona.src.main.community.interfaces.CommunityRetrofitInterface;
import com.softsquared.android.corona.src.main.community.interfaces.PostDetailView;
import com.softsquared.android.corona.src.main.community.model.PostDetailResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;

public class PostDetailService {

    private PostDetailView mPostDetailView;

    public PostDetailService(PostDetailView postDetailView){
        mPostDetailView = postDetailView;
    }

    void getPostDetail(int postNo){
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.getPostDetail(postNo).enqueue(new Callback<PostDetailResponse>() {
            @Override
            public void onResponse(Call<PostDetailResponse> call, Response<PostDetailResponse> response) {
                final PostDetailResponse postDetailResponse = response.body();
                if(postDetailResponse==null){
                    mPostDetailView.validateFailure(null);
                }
                else if(postDetailResponse.getCode()==100){
                    mPostDetailView.getPostDetail(postDetailResponse.getResult(), postDetailResponse.getComments());
                }
                else{
                    mPostDetailView.validateFailure(postDetailResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostDetailResponse> call, Throwable t) {
                mPostDetailView.validateFailure(null);
            }
        });
    }
}
