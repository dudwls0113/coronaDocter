package com.softsquared.android.corona.src.main.map.interfaces;

import com.softsquared.android.corona.src.main.map.models.RouteResponse;

import java.util.ArrayList;

public interface MapViewView {

    void validateGetRouteSuccess(ArrayList<RouteResponse> arrayList);

    void validateGetRouteFail(String message);
}
