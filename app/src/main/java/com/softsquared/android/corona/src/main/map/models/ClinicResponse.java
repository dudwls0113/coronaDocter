package com.softsquared.android.corona.src.main.map.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClinicResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    ArrayList<ClinicInfo> clinicInfos;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<ClinicInfo> getClinicInfos() {
        return clinicInfos;
    }
}
