package com.softsquared.android.coronadoctor.src.main.info.models;

import com.google.gson.annotations.SerializedName;

public class InfoResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    Info info;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Info getInfo() {
        return info;
    }
}
