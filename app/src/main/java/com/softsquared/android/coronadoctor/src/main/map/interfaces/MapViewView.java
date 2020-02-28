package com.softsquared.android.coronadoctor.src.main.map.interfaces;

import com.softsquared.android.coronadoctor.src.main.map.models.ClinicInfo;
import com.softsquared.android.coronadoctor.src.main.map.models.HospitalInfo;
import com.softsquared.android.coronadoctor.src.main.map.models.Infected;
import com.softsquared.android.coronadoctor.src.main.map.models.RouteResponse;

import java.util.ArrayList;

public interface MapViewView {

    void validateGetRouteSuccess(ArrayList<RouteResponse> arrayList);

    void validateGetRouteFail(String message);

    void validateGetClinicSuccess(ArrayList<ClinicInfo> clinicInfos);

    void validateGetHospitalSuccess(ArrayList<HospitalInfo> arrayList);

    void validateGetInfectedSuccess(ArrayList<Infected> arrayList);

}
