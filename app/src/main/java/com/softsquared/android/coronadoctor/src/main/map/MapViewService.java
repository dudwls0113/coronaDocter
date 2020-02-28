package com.softsquared.android.coronadoctor.src.main.map;

import android.util.Log;

import com.softsquared.android.coronadoctor.src.main.map.interfaces.MapViewRetrofitInterface;
import com.softsquared.android.coronadoctor.src.main.map.interfaces.MapViewView;
import com.softsquared.android.coronadoctor.src.main.map.models.ClinicResponse;
import com.softsquared.android.coronadoctor.src.main.map.models.HospitalResponse;
import com.softsquared.android.coronadoctor.src.main.map.models.InfectedResponse;
import com.softsquared.android.coronadoctor.src.main.map.models.MapViewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.coronadoctor.src.ApplicationClass.getRetrofit;

public class MapViewService {

    private final MapViewView mMapViewView;

    MapViewService(final MapViewView mapViewView) {
        this.mMapViewView = mapViewView;
    }

    void getRoute() {
        final MapViewRetrofitInterface mapViewRetrofitInterface = getRetrofit().create(MapViewRetrofitInterface.class);
        mapViewRetrofitInterface.getRoute().enqueue(new Callback<MapViewResponse>() {
            @Override
            public void onResponse(Call<MapViewResponse> call, Response<MapViewResponse> response) {
                final MapViewResponse mapViewResponse = response.body();
                if (mapViewResponse == null) {
                    Log.e("에러2", "에러2");
                    mMapViewView.validateGetRouteFail(null);
                } else if (mapViewResponse.getCode() == 100) {
                    mMapViewView.validateGetRouteSuccess(mapViewResponse.getArrayList());
                } else {
                    mMapViewView.validateGetRouteFail(mapViewResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<MapViewResponse> call, Throwable t) {
                mMapViewView.validateGetRouteFail(null);
                Log.e("에러", t.toString());
            }
        });
    }

    void getClinic() {
        final MapViewRetrofitInterface mapViewRetrofitInterface = getRetrofit().create(MapViewRetrofitInterface.class);
        mapViewRetrofitInterface.getClinic().enqueue(new Callback<ClinicResponse>() {
            @Override
            public void onResponse(Call<ClinicResponse> call, Response<ClinicResponse> response) {
                final ClinicResponse clinicResponse = response.body();
                if (clinicResponse == null) {
                    mMapViewView.validateGetRouteFail(null);
                } else if (clinicResponse.getCode() == 100) {
                    mMapViewView.validateGetClinicSuccess(clinicResponse.getClinicInfos());
                } else {
                    mMapViewView.validateGetRouteFail(clinicResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ClinicResponse> call, Throwable t) {
                mMapViewView.validateGetRouteFail(null);
            }
        });
    }

    void getHospital() {
        final MapViewRetrofitInterface mapViewRetrofitInterface = getRetrofit().create(MapViewRetrofitInterface.class);
        mapViewRetrofitInterface.getHospital().enqueue(new Callback<HospitalResponse>() {
            @Override
            public void onResponse(Call<HospitalResponse> call, Response<HospitalResponse> response) {
                final HospitalResponse hospitalResponse = response.body();
                if (hospitalResponse == null) {
                    mMapViewView.validateGetRouteFail(null);
                } else if (hospitalResponse.getCode() == 100) {
                    mMapViewView.validateGetHospitalSuccess(hospitalResponse.getHospitalInfos());
                } else {
                    mMapViewView.validateGetRouteFail(hospitalResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<HospitalResponse> call, Throwable t) {
                mMapViewView.validateGetRouteFail(null);
            }
        });
    }

    void getInfected() {
        final MapViewRetrofitInterface mapViewRetrofitInterface = getRetrofit().create(MapViewRetrofitInterface.class);
        mapViewRetrofitInterface.getInfectedList().enqueue(new Callback<InfectedResponse>() {
            @Override
            public void onResponse(Call<InfectedResponse> call, Response<InfectedResponse> response) {
                final InfectedResponse infectedResponse = response.body();
                if (infectedResponse == null) {
                    mMapViewView.validateGetRouteFail(null);
                } else if (infectedResponse.getCode() == 100) {
                    mMapViewView.validateGetInfectedSuccess(infectedResponse.getArrayList());
                } else {
                    mMapViewView.validateGetRouteFail(infectedResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<InfectedResponse> call, Throwable t) {
                mMapViewView.validateGetRouteFail(null);
            }
        });
    }
}
