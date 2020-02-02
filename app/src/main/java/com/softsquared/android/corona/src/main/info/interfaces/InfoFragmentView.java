package com.softsquared.android.corona.src.main.info.interfaces;


import com.softsquared.android.corona.src.main.info.models.Info;

public interface InfoFragmentView {

    void validateSuccess(Info info);

    void validateFailure(String message);
}
