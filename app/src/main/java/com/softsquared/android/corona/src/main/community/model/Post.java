package com.softsquared.android.corona.src.main.community.model;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("likeCount")
    private int likeCount;

    @SerializedName("commentCount")
    private int commentCount;

    @SerializedName("postNo")
    int postNo;

    @SerializedName("createdAt")
    String createdAt;

    @SerializedName("type")
    int type;

    public void setLikeCountPlus() {
        this.likeCount = likeCount + 1;
    }

    public int getPostNo() {
        return postNo;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public Post(String title, String content, int likeCount, int commentCount) {
        this.title = title;
        this.content = content;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
