package com.softsquared.android.corona.src.main.info;


import com.softsquared.android.corona.src.main.info.interfaces.InfoFragmentView;
import com.softsquared.android.corona.src.main.info.interfaces.InfoRetrofitInterface;
import com.softsquared.android.corona.src.main.info.models.Info;
import com.softsquared.android.corona.src.main.info.models.InfoResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.softsquared.android.corona.src.ApplicationClass.getRetrofit;

public class InfoFragmentService {

    private final InfoFragmentView mInfoFragmentView;

    InfoFragmentService(InfoFragmentView infoFragmentView){
        this.mInfoFragmentView = infoFragmentView;
    }

    void getStatistics(){
        final InfoRetrofitInterface infoRetrofitInterface = getRetrofit().create(InfoRetrofitInterface.class);
        infoRetrofitInterface.getStatistics().enqueue(new Callback<InfoResponse>() {
            @Override
            public void onResponse(Call<InfoResponse> call, Response<InfoResponse> response) {
                final InfoResponse infoResponse = response.body();
                if(infoResponse==null){
                    mInfoFragmentView.validateFailure(null);
                }
                else if(infoResponse.getCode()==100){
                    mInfoFragmentView.validateSuccess(infoResponse.getInfo());
                }
                else{
                    mInfoFragmentView.validateFailure(infoResponse.getMessage());
                }
            }

            @Override
            public void onFailure(Call<InfoResponse> call, Throwable t) {
                mInfoFragmentView.validateFailure(null);
            }
        });
    }
}
