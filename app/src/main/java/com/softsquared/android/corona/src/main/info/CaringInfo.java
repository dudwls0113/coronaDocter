package com.softsquared.android.corona.src.main.info;

import com.google.gson.annotations.SerializedName;

public class CaringInfo {

    private int caringId;

    private String title;

    private int type;


    public void setCaringId(int caringId) {
        this.caringId = caringId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCaringId() {
        return caringId;
    }

    public String getTitle() {
        return title;
    }

    public int getType() {
        return type;
    }

    public CaringInfo(int caringId, String title, int type) {
        this.caringId = caringId;
        this.title = title;
        this.type = type;
    }
}
