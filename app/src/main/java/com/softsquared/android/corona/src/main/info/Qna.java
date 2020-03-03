package com.softsquared.android.corona.src.main.info;

import com.google.gson.annotations.SerializedName;

public class Qna {
    @SerializedName("name")
    String name;
    @SerializedName("description")
    String description;

    public Qna(String title, String answer) {
        this.name = title;
        this.description = answer;
    }

    public String getTitle() {
        return name;
    }

    public void setTitle(String title) {
        this.name = title;
    }

    public void setAnswer(String answer) {
        this.description = answer;
    }

    public String getAnswer() {
        return description;
    }
}
