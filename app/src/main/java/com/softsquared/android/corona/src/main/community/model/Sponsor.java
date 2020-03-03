package com.softsquared.android.corona.src.main.community.model;

import com.google.gson.annotations.SerializedName;

public class Sponsor {
    public Sponsor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @SerializedName("name")
    String name;

}
