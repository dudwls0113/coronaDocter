package com.softsquared.android.corona.src;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.softsquared.android.corona.R;
import com.softsquared.android.corona.src.main.MainActivity;
import com.softsquared.android.corona.src.splash.SplashActivity;

import java.util.Objects;

import static com.softsquared.android.corona.src.ApplicationClass.PUSH_ON_OFF;
import static com.softsquared.android.corona.src.ApplicationClass.channel_id;


public class FireBaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
//        Log.e("Firebase", "FireBaseMessagingService : " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("FCM", "aaaaaa");
        SharedPreferences spf = this.getApplicationContext().getSharedPreferences("CORONA_DOCTOR_APP", Context.MODE_PRIVATE);
        boolean isPushOn = spf.getBoolean(PUSH_ON_OFF, true);
        Log.d("서비스", isPushOn + "");
        if (isPushOn && remoteMessage != null && remoteMessage.getData().get("title").length() > 1) {
            Log.d("message", "Message Notification Body: " + remoteMessage.getData().get("message"));
            sendNotification(remoteMessage);
        }
    }

    private void sendNotification(RemoteMessage remoteMessage) {
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String type = remoteMessage.getData().get("type");

        PendingIntent resultPendingIntent;


        if (remoteMessage.getData().get("postNo") != null && remoteMessage.getData().get("postNo").length() > 0) {
            int postNo = Integer.parseInt(remoteMessage.getData().get("postNo"));
            resultPendingIntent = getPendingIntentToPostNo(postNo);
        } else {
            resultPendingIntent = getPendingIntent(type);
        }
        final String CHANNEL_ID = "coronaDoctor";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "coronaDoctor_channel";
            final String CHANNEL_DESCRIPTION = "fcm channel of coronaDoctor";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(mChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setWhen(System.currentTimeMillis());

        builder.setSmallIcon(R.drawable.ic_stat_name);
        if (android.os.Build.VERSION.SDK_INT >= 28) {   // Android P
            builder.setSmallIcon(R.drawable.ic_stat_name);
        } else {
            builder.setSmallIcon(R.drawable.icon_asset);
        }
        builder.setColor(0xffffff);

        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSound(defaultSoundUri);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));   // 3
        builder.setContentIntent(resultPendingIntent);
//        builder.setFullScreenIntent(pendingIntent, true);

        notificationManager.notify(channel_id, builder.build());
        channel_id++;
        if (channel_id == 100) {
            channel_id = 0;
        }
    }

    private PendingIntent getPendingIntent(String type) {
        Intent backIntent = new Intent(this, SplashActivity.class);
        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent resultIntent;

        if (type == null) {
            Log.d("로그", "null");
        }
        //알람을 클릭하면 타입에 따라 해당 화면으로 이동합니다.
        else if (type.equals("COMMUNITY")) {
            Log.d("로그", "COMMUNITY");
            backIntent.putExtra("moveCommunityTab", true);

            return PendingIntent.getActivities(
                    this, 0,
                    new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);
        }
        return PendingIntent.getActivities(this, 0, new Intent[]{backIntent}, PendingIntent.FLAG_ONE_SHOT);
    }

    private PendingIntent getPendingIntentToPostNo(int postNo) {
//        Intent backIntent = new Intent(this, SplashActivity.class);
//        backIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        Log.d("로그", "" + postNo);
////        backIntent.putExtra("moveCommunityTab", true);
//        backIntent.putExtra("postNo", postNo);

        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(SplashActivity.class);
        stackBuilder.addNextIntent(intent);

        intent.putExtra("postNo", postNo);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(channel_id, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }
}
