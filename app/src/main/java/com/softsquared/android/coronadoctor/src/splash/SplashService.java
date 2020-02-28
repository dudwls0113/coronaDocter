package com.softsquared.android.coronadoctor.src.splash;


import android.util.Log;

import com.softsquared.android.coronadoctor.src.main.models.DefaultResponse;
import com.softsquared.android.coronadoctor.src.splash.interfaces.SplashActivityView;
import com.softsquared.android.coronadoctor.src.splash.interfaces.SplashRetrofitInterface;
import com.softsquared.android.coronadoctor.src.splash.models.UpdateCheckResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.coronadoctor.src.ApplicationClass.getRetrofit;


class SplashService {
    private final SplashActivityView mSplashActivityView;

    SplashService(final SplashActivityView mSplashActivityView) {
        this.mSplashActivityView = mSplashActivityView;
    }

    void checkUpdate(int versionCode) {
        final SplashRetrofitInterface splashRetrofitInterface = getRetrofit().create(SplashRetrofitInterface.class);
        splashRetrofitInterface.updateCheck(versionCode).enqueue(new Callback<UpdateCheckResponse>() {
            @Override
            public void onResponse(Call<UpdateCheckResponse> call, Response<UpdateCheckResponse> response) {
                final UpdateCheckResponse updateCheckResponse = response.body();
                if (updateCheckResponse == null) {
                    mSplashActivityView.validateFailure(null);
                } else if (updateCheckResponse.getCode() == 100) {
                    mSplashActivityView.updateCheckSuccess(updateCheckResponse.getUpdateMode());
                } else {
                    mSplashActivityView.validateFailure(updateCheckResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<UpdateCheckResponse> call, Throwable t) {
                mSplashActivityView.validateFailure(null);
            }
        });
    }

    void updateFcmToken(String fcmToken) {
        final SplashRetrofitInterface splashRetrofitInterface = getRetrofit().create(SplashRetrofitInterface.class);
        splashRetrofitInterface.updateFcm(fcmToken).enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                final DefaultResponse defaultResponse = response.body();
                if (defaultResponse == null) {
                    Log.d("FCM", "서버저장 실패2");
                    mSplashActivityView.updateFcmFail();
                } else if (defaultResponse.getCode() == 100) {
                    Log.d("FCM", "서버저장 성공" + defaultResponse.getMessage());
                    mSplashActivityView.updateFcmSuccess(defaultResponse.getFcmToken());
                } else {
                    Log.d("FCM", "서버저장 실패3");
                    mSplashActivityView.updateFcmFail();
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Log.d("FCM", "서버저장 실패 4");
                mSplashActivityView.updateFcmFail();
            }
        });
    }
}
