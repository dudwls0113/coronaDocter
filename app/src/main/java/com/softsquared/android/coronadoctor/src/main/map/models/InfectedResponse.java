package com.softsquared.android.coronadoctor.src.main.map.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class InfectedResponse {
    @SerializedName("count")
    ArrayList<Infected> arrayList;

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    public ArrayList<Infected> getArrayList() {
        return arrayList;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

