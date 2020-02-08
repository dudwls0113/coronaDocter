package com.softsquared.android.corona.src.main.community;

import com.softsquared.android.corona.src.main.community.interfaces.CommunityRetrofitInterface;
import com.softsquared.android.corona.src.main.community.interfaces.CommunityView;
import com.softsquared.android.corona.src.main.community.model.PostResponse;
import com.softsquared.android.corona.src.main.models.DefaultResponse;
import com.softsquared.android.corona.src.main.community.model.PostWriteResponse;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.MEDIA_TYPE_JSON;
import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;

public class CommunityService {
    private final CommunityView mCommunityView;

    public CommunityService(CommunityView communityView) {
        mCommunityView = communityView;
    }

    void getHotPost() {
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.getHotPost().enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                final PostResponse postResponse = response.body();
                if (postResponse == null) {
                    mCommunityView.validateFailure(null);
                } else if (postResponse.getCode() == 100) {
                    mCommunityView.getHotPostSuccess(postResponse.getmPosts());
                } else {
                    mCommunityView.validateFailure(postResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }

    void getNewPost(int page) {
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.getNewPost(page).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                final PostResponse postResponse = response.body();
                if (postResponse == null) {
                    mCommunityView.validateFailure(null);
                } else if (postResponse.getCode() == 100) {
                    mCommunityView.getNewPostSuccess(postResponse.getmPosts());
                } else {
                    mCommunityView.validateFailure(postResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }

    void postLike(int postNo, final String fcmToken, final int mode, final int position) {
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
                    mCommunityView.validateFailure(null);
                } else if (defaultResponse.getCode() == 100) {
                    mCommunityView.postLikeSuccess(mode, position);
                } else {
                    mCommunityView.validateFailure(defaultResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }

    void postWritePost(String fcmToken, String title, String content) {
        JSONObject params = new JSONObject();
        try {
            params.put("fcmToken", fcmToken);
            params.put("title", title);
            params.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final CommunityRetrofitInterface communityRetrofitInterface = getRetrofit().create(CommunityRetrofitInterface.class);
        communityRetrofitInterface.postWrite(RequestBody.create(params.toString(), MEDIA_TYPE_JSON)).enqueue(new Callback<PostWriteResponse>() {
            @Override
            public void onResponse(Call<PostWriteResponse> call, Response<PostWriteResponse> response) {
                final PostWriteResponse postWriteResponse = response.body();
                if (postWriteResponse == null) {
                    mCommunityView.validateFailure(null);
                } else if (postWriteResponse.getCode() == 100) {
                    mCommunityView.postWritePostSuccess();
                } else {
                    mCommunityView.validateFailure(postWriteResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<PostWriteResponse> call, Throwable t) {
                mCommunityView.validateFailure(null);
            }
        });
    }
}
