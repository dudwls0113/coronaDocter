package com.softsquared.android.coronadoctor.src.main.community.model;

import com.google.gson.annotations.SerializedName;

public class PostWriteResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
