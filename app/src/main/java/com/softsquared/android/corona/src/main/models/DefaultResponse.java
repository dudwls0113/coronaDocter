package com.softsquared.android.corona.src.main.models;

import com.google.gson.annotations.SerializedName;

public class DefaultResponse {
    @SerializedName("code")
    private int code;

    @SerializedName("message")
    private String message;

    @SerializedName("fcmToken")
    private String fcmToken;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    //    public boolean getIsSuccess() {
//        return isSuccess;
//    }
}