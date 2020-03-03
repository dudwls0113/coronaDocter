package com.softsquared.android.corona.src.main.info.interfaces;


import com.softsquared.android.corona.src.main.info.models.InfectedData;
import com.softsquared.android.corona.src.main.info.models.Info;

import java.util.ArrayList;

public interface InfoFragmentView {

    void validateSuccess(Info info);

    void getGraphSuccess(ArrayList<InfectedData> info);


    void validateFailure(String message);
}
