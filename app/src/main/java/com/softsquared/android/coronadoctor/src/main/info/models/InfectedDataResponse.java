package com.softsquared.android.coronadoctor.src.main.info.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InfectedDataResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    ArrayList<InfectedData> info;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<InfectedData> getData() {
        return info;
    }
}
