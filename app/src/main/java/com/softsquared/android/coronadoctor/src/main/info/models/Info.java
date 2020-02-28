package com.softsquared.android.coronadoctor.src.main.info.models;

import com.google.gson.annotations.SerializedName;

public class Info {
    @SerializedName("infected")
    int infected;

    @SerializedName("dead")
    int dead;

    @SerializedName("cured")
    int cured;

    @SerializedName("symptom")
    int symptom;

    @SerializedName("inspection")
    int inspection;

    @SerializedName("isolation")
    int isolation;

    @SerializedName("infectedDiff")
    int infectedDiff;

    @SerializedName("deadDiff")
    int deadDiff;

    @SerializedName("curedDiff")
    int curedDiff;

    @SerializedName("symptomDiff")
    int symptomDiff;

    @SerializedName("inspectionDiff")
    int inspectionDiff;

    @SerializedName("isolationDiff")
    int isolationDiff;

    @SerializedName("updatedTime")
    String updatedTime;

    public int getInfected() {
        return infected;
    }

    public int getDead() {
        return dead;
    }

    public int getCured() {
        return cured;
    }

    public int getSymptom() {
        return symptom;
    }

    public int getInspection() {
        return inspection;
    }

    public int getIsolation() {
        return isolation;
    }

    public int getInfectedDiff() {
        return infectedDiff;
    }

    public int getDeadDiff() {
        return deadDiff;
    }

    public int getCuredDiff() {
        return curedDiff;
    }

    public int getSymptomDiff() {
        return symptomDiff;
    }

    public int getInspectionDiff() {
        return inspectionDiff;
    }

    public int getIsolationDiff() {
        return isolationDiff;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }
}
