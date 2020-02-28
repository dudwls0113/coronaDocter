package com.softsquared.android.coronadoctor.src.main.map.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class HospitalResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    ArrayList<HospitalInfo> hospitalInfos;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<HospitalInfo> getHospitalInfos() {
        return hospitalInfos;
    }
}
