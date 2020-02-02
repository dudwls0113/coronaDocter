package com.softsquared.android.corona.src.main.info.interfaces;

import com.softsquared.android.corona.src.main.info.models.InfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InfoRetrofitInterface {

    @GET("/corona/statistic")
    Call<InfoResponse> getStatistics();
}
