package com.softsquared.android.coronadoctor.src.main.info.models;

import com.google.gson.annotations.SerializedName;

public class InfectedData {
    @SerializedName("dayInfected")
    private int dayInfected;
    @SerializedName("totalInfected")
    private int totalInfected;
    @SerializedName("date")
    private String date;

    public String getDate() {
        return date;
    }

    public InfectedData(int dayInfected, int totalInfected, String date) {
        this.dayInfected = dayInfected;
        this.totalInfected = totalInfected;
        this.date = date;
    }

    public int getDayInfected() {
        return dayInfected;
    }

    public int getTotalInfected() {
        return totalInfected;
    }
}