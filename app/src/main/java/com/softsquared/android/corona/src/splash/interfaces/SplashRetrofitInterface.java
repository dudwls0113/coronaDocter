package com.softsquared.android.corona.src.splash.interfaces;
import com.softsquared.android.corona.src.splash.models.UpdateCheckResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SplashRetrofitInterface {
//    @GET("/test")

    @GET("/corona/update/{versionCode}")
    Call<UpdateCheckResponse> updateCheck(@Path("versionCode") final int versionCode);
}
