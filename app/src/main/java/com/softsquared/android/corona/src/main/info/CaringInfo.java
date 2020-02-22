package com.softsquared.android.corona.src.main.info;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CaringInfo {

    private int caringId;

    private String title;

    private int type;

    private ArrayList<String> arrayListName;

    public ArrayList<String> getArrayListName() {
        return arrayListName;
    }

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

    public CaringInfo(int caringId, String title, int type, ArrayList<String> arrayListName) {
        this.caringId = caringId;
        this.title = title;
        this.type = type;
        this.arrayListName = arrayListName;
    }

    public CaringInfo(int caringId, String title, int type) {
        this.caringId = caringId;
        this.title = title;
        this.type = type;
    }
}
