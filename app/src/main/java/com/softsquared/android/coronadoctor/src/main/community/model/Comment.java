package com.softsquared.android.coronadoctor.src.main.community.model;

import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("commentNo")
    int commentNo;

    @SerializedName("comment")
    String comment;

    @SerializedName("createdAt")
    String createdAt;

    public int getCommentNo() {
        return commentNo;
    }

    public String getComment() {
        return comment;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
