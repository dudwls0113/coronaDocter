package com.softsquared.android.corona.src.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.BaseActivity;
import com.softsquared.android.corona.src.main.MainActivity;
import com.softsquared.android.corona.src.splash.interfaces.SplashActivityView;
import com.facebook.FacebookSdk;

import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_CLINIC;
import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_HOSPITAL;
import static com.softsquared.android.corona.src.ApplicationClass.CAN_UPDATE_ROUTE;
import static com.softsquared.android.corona.src.ApplicationClass.sSharedPreferences;

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
                getFcmToken();
//                initMapUpdate();
                checkUpdate(versionCode);
            }
        });
        thread.start();
    }

    void checkUpdate(int versionCode) {
        final SplashService signInService = new SplashService(this);
        signInService.checkUpdate(versionCode);
    }

    void updateFcmToken(String fcmToken) {
        final SplashService signInService = new SplashService(this);
        signInService.updateFcmToken(fcmToken);
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

    @Override
    public void updateFcmFail() {

    }

    @Override
    public void updateFcmSuccess(String fcm) {
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putString("fcm", fcm);
        editor.apply();

    }

    public void getFcmToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();

                        SharedPreferences spf = sSharedPreferences;
                        String savedToken = spf.getString("fcm", null);
                        Log.d("FCM", "저장된 토큰: " + savedToken);
                        Log.d("FCM", "발급받은 토큰: " + token);

                        if (savedToken == null) {//fcm토큰을 처음발급받을때
                            updateFcmToken(token);
                            Log.d("FCM", "토큰이 없어서 새로저장");
                        } else if (!token.equals(savedToken)) {//토큰이 바뀜
                            updateFcmToken(token);
                            Log.d("FCM", "토큰이 바뀌어서 새로저장");
                        }
                    }
                });
    }

    public void initMapUpdate() {
        final SharedPreferences.Editor editor = sSharedPreferences.edit();
        editor.putBoolean(CAN_UPDATE_ROUTE, true);
        editor.putBoolean(CAN_UPDATE_CLINIC, true);
        editor.putBoolean(CAN_UPDATE_HOSPITAL, true);
        editor.apply();
    }


}
