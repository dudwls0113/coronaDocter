package com.softsquared.android.corona.src.main.community.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


public class PostDetailResponse {

    @SerializedName("code")
    int code;

    @SerializedName("message")
    String message;

    @SerializedName("result")
    Post result;

    @SerializedName("comment")
    ArrayList<Comment> comments;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Post getResult() {
        return result;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
