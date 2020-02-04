package com.softsquared.android.corona.src.splash;


import com.softsquared.android.corona.src.main.models.DefaultResponse;
import com.softsquared.android.corona.src.splash.interfaces.SplashActivityView;
import com.softsquared.android.corona.src.splash.interfaces.SplashRetrofitInterface;
import com.softsquared.android.corona.src.splash.models.UpdateCheckResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;


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
}
