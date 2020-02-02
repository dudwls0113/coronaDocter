package com.softsquared.android.corona.src.main.map.interfaces;

import com.softsquared.android.corona.src.main.map.models.MapViewResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapViewRetrofitInterface {

    @GET("/corona/route")
    Call<MapViewResponse> getRoute();
}
