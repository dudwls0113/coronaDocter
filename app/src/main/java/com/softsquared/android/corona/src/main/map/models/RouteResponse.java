package com.softsquared.android.corona.src.main.map.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RouteResponse {

    @SerializedName("route")
    ArrayList<RouteRes> routeRes;

    public ArrayList<RouteRes> getRouteRes() {
        return routeRes;
    }
}
