package com.softsquared.android.corona.src.splash.interfaces;

public interface SplashActivityView {

    void updateCheckSuccess(int updateMode);

    void validateFailure(String message);

    void updateFcmFail();

    void updateFcmSuccess(String fcm);

}
