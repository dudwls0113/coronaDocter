package com.softsquared.android.coronadoctor.src.main.map.models;

import com.google.gson.annotations.SerializedName;

public class HospitalInfo {
    @SerializedName("hospitalNo")
    int hospitalNo;

    @SerializedName("name")
    String name;

    @SerializedName("mobile")
    String mobile;

    @SerializedName("patientCount")
    int patientCount;

    @SerializedName("latitude")
    double latitude;

    @SerializedName("longitude")
    double longitude;

    public int getHospitalNo() {
        return hospitalNo;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public int getPatientCount() {
        return patientCount;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
