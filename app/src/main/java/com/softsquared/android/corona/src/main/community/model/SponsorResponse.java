package com.softsquared.android.corona.src.main.community.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SponsorResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    ArrayList<Sponsor> mSponsorList;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Sponsor> getmSponsorList() {
        return mSponsorList;
    }
}
