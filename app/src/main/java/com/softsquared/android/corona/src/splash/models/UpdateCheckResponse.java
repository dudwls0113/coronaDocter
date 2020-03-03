package com.softsquared.android.corona.src.splash.models;

import com.google.gson.annotations.SerializedName;

public class UpdateCheckResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("updateMode")
    private int updateMode;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public int getUpdateMode() {
        return updateMode;
    }
}