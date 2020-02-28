package com.softsquared.android.coronadoctor.src.splash.interfaces;
import com.softsquared.android.coronadoctor.src.main.models.DefaultResponse;
import com.softsquared.android.coronadoctor.src.splash.models.UpdateCheckResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SplashRetrofitInterface {
//    @GET("/test")

    @GET("/corona/update/{versionCode}")
    Call<UpdateCheckResponse> updateCheck(@Path("versionCode") final int versionCode);

    @GET("/corona/fcm/{fcmToken}")
    Call<DefaultResponse> updateFcm(@Path("fcmToken") final String fcmToken);

}
