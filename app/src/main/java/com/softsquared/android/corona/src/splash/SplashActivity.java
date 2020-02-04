package com.softsquared.android.corona.src.splash;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.main.MainActivity;
import com.softsquared.android.corona.src.splash.interfaces.SplashActivityView;
import com.facebook.FacebookSdk;

public class SplashActivity extends BaseActivity implements SplashActivityView {

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PackageInfo pi = null;
                try {
                    pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                assert pi != null;
                int versionCode = pi.versionCode;
                checkUpdate(versionCode);
            }
        });
        thread.start();
    }

    void checkUpdate(int versionCode) {
        final SplashService signInService = new SplashService(this);
        signInService.checkUpdate(versionCode);
    }


    @Override
    public void updateCheckSuccess(int updateMode) {
        switch (updateMode) {
            case 1:
                MustUpdateDialog mustUpdateDialog = new MustUpdateDialog(mContext, new MustUpdateDialog.CustomLIstener() {
                    @Override
                    public void goUpdateClick() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.softsquared.android.corona")));
                        finish();
                    }

                    @Override
                    public void cancelClick() {
                        finish();
                    }
                });
                mustUpdateDialog.show();
                break;

            case 2:
                UpdateDialog updateDialog = new UpdateDialog(mContext, new UpdateDialog.CustomLIstener() {
                    @Override
                    public void goUpdateClick() {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.softsquared.android.corona")));
                        finish();
                    }

                    @Override
                    public void cancelClick() {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });
                updateDialog.show();

                break;
            case 3:
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void validateFailure(String message) {
        showCustomToast(message);
        finish();
    }
}
