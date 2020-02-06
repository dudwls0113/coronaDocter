package com.softsquared.android.corona.src.main.map.models;

import com.google.gson.annotations.SerializedName;

public class RouteRes {

    @SerializedName("infectedNo")
    int infectedNo;

    @SerializedName("name")
    String name;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longtitude;

    @SerializedName("content")
    String content;

    @SerializedName("date")
    String date;

    @SerializedName("isolationDate")
    String isolationDate;

    @SerializedName("hospital")
    String hospital;

    @SerializedName("contactCount")
    int contactCount;

    @SerializedName("age")
    int age;

    @SerializedName("routeName")
    String routeName;

    public int getNewRoute() {
        return isNewRoute;
    }

    @SerializedName("isNewRoute")
    int isNewRoute;

    public String getRouteName() {
        return routeName;
    }

    public int getInfectedNo() {
        return infectedNo;
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getIsolationDate() {
        return isolationDate;
    }

    public String getHospital() {
        return hospital;
    }

    public int getContactCount() {
        return contactCount;
    }

    public int getAge() {
        return age;
    }
}
