package com.softsquared.android.corona.src.main.community.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    ArrayList<Post> mPosts;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Post> getmPosts() {
        return mPosts;
    }
}
