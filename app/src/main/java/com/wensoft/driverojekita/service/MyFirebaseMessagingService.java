package com.wensoft.driverojekita.service;

/**
 * Created by farhan on 1/22/17.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.wensoft.driverojekita.R;
import com.wensoft.driverojekita.main.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private String title, body;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        title = remoteMessage.getData().get("title");
        body = remoteMessage.getData().get("body");
        //sendNotification(remoteMessage.getNotification().getBody());
        sendNotification(title,body);
        Log.d("TOKEN", "Pesan " + remoteMessage.getData().get("body"));
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("message",body);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}