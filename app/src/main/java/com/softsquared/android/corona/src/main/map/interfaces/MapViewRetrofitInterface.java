package com.softsquared.android.corona.src.main.map.interfaces;

import com.softsquared.android.corona.src.main.map.models.ClinicResponse;
import com.softsquared.android.corona.src.main.map.models.HospitalResponse;
import com.softsquared.android.corona.src.main.map.models.InfectedResponse;
import com.softsquared.android.corona.src.main.map.models.MapViewResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapViewRetrofitInterface {

    @GET("/corona/route")
    Call<MapViewResponse> getRoute();

    @GET("/corona/clinic")
    Call<ClinicResponse> getClinic();

    @GET("/corona/hospital")
    Call<HospitalResponse> getHospital();

    @GET("/corona/infected")
    Call<InfectedResponse> getInfectedList();
}
