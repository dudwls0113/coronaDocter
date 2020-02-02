package com.softsquared.android.corona.src.main.map;

import android.util.Log;

import com.softsquared.android.corona.src.main.map.interfaces.MapViewRetrofitInterface;
import com.softsquared.android.corona.src.main.map.interfaces.MapViewView;
import com.softsquared.android.corona.src.main.map.models.MapViewResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;

public class MapViewService {

    private final MapViewView mMapViewView;

    MapViewService(final MapViewView mapViewView){
        this.mMapViewView = mapViewView;
    }

    void getRoute(){
        final MapViewRetrofitInterface mapViewRetrofitInterface = getRetrofit().create(MapViewRetrofitInterface.class);
        mapViewRetrofitInterface.getRoute().enqueue(new Callback<MapViewResponse>() {
            @Override
            public void onResponse(Call<MapViewResponse> call, Response<MapViewResponse> response) {
                final MapViewResponse mapViewResponse = response.body();
                if(mapViewResponse==null){
                    Log.e("에러2", "에러2");
                    mMapViewView.validateGetRouteFail(null);
                }
                else if(mapViewResponse.getCode()==100){
                    mMapViewView.validateGetRouteSuccess(mapViewResponse.getArrayList());
                }
                else{
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
}
