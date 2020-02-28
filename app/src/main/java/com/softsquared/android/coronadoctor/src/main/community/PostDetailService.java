package com.softsquared.android.coronadoctor.src.main.community;

import com.softsquared.android.coronadoctor.src.main.community.interfaces.CommunityRetrofitInterface;
import com.softsquared.android.coronadoctor.src.main.community.interfaces.PostDetailView;
import com.softsquared.android.coronadoctor.src.main.community.model.PostDetailResponse;
import com.softsquared.android.coronadoctor.src.main.community.model.PostWriteResponse;
import com.softsquared.android.coronadoctor.src.main.models.DefaultResponse;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.coronadoctor.src.ApplicationClass.MEDIA_TYPE_JSON;
import static com.softsquared.android.coronadoctor.src.ApplicationClass.getRetrofit;

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

    void postCommentWrite(String fcmToken, int postNo, String content){
        JSONObject params = new JSONObject();
        try {
            params.put("fcmToken", fcmToken);
            params.put("postNo", postNo);
            params.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.postWriteComment(RequestBody.create(params.toString(),MEDIA_TYPE_JSON)).enqueue(new Callback<PostWriteResponse>() {
            @Override
            public void onResponse(Call<PostWriteResponse> call, Response<PostWriteResponse> response) {
                final PostWriteResponse postWriteResponse = response.body();
                if(postWriteResponse==null){
                    mPostDetailView.validateFailure(null);
                }
                else if(postWriteResponse.getCode()==100){
                    mPostDetailView.postCommentWrite();
                }
                else{
                    mPostDetailView.validateFailure(postWriteResponse.getMessage());
                    System.out.println(postWriteResponse.getCode()+"코드");
                }
            }

            @Override
            public void onFailure(Call<PostWriteResponse> call, Throwable t) {
                mPostDetailView.validateFailure(null);
            }
        });
    }

    void postLike(int postNo, final String fcmToken) {
        JSONObject params = new JSONObject();
        try {
            params.put("postNo", postNo);
            params.put("fcmToken", fcmToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.postLike(RequestBody.create(params.toString(), MEDIA_TYPE_JSON)).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                final DefaultResponse defaultResponse = response.body();
                if (defaultResponse == null) {
                    mPostDetailView.validateFailure(null);
                } else if (defaultResponse.getCode() == 100) {
                    mPostDetailView.postLikeSuccess();
                } else {
                    mPostDetailView.validateFailure(defaultResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                mPostDetailView.validateFailure(null);
            }
        });
    }

    void postReport(int postNo, final String fcmToken){
        JSONObject params = new JSONObject();
        try {
            params.put("postNo", postNo);
            params.put("fcmToken", fcmToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.postReport(RequestBody.create(params.toString(),MEDIA_TYPE_JSON)).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                final DefaultResponse defaultResponse = response.body();
                if (defaultResponse == null) {
                    mPostDetailView.validateFailure(null);
                } else if (defaultResponse.getCode() == 100) {
                    mPostDetailView.postReportSuccess();
                } else {
                    mPostDetailView.validateFailure(defaultResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                mPostDetailView.validateFailure(null);
            }
        });
    }
}
