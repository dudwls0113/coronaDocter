package com.softsquared.android.corona.src.main.map.models;

        import com.google.gson.annotations.SerializedName;

public class Infected {
    @SerializedName("name")
    String name;

    @SerializedName("isUpdated")
    int isUpdated;

    @SerializedName("infectedNo")
    int infectedNo;

    public int getInfectedNo() {
        return infectedNo;
    }

    public int getIsUpdated() {
        return isUpdated;
    }

    boolean isSelected = true;

    public String getName() {
        return name + "번째 확진자";
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}

