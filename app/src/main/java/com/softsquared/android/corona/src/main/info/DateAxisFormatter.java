package com.softsquared.android.corona.src.main.info;

import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

public class DateAxisFormatter extends IndexAxisValueFormatter {
    private  String[] mDate;
    private int mDateCount = 0;

    public DateAxisFormatter(String[] date){
        mDate = date;
        mDateCount = mDate.length;
    }

    @Override
    public String getFormattedValue(float value) {
        return mDate[((int)value)];
    }
}
