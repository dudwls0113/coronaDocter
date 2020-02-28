package com.softsquared.android.coronadoctor.src.main.map.models;

import com.google.gson.annotations.SerializedName;

public class ClinicInfo {

    @SerializedName("clinicNo")
    int clinicNo;

    @SerializedName("city")
    String city;

    @SerializedName("gu")
    String gu;

    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    public int getClinicNo() {
        return clinicNo;
    }

    public String getCity() {
        return city;
    }

    public String getGu() {
        return gu;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
