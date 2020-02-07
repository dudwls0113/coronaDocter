package com.softsquared.android.corona.src.main.community.model;

public class Post {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
