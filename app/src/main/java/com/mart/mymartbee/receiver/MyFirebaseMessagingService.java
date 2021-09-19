package com.mart.mymartbee.receiver;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        createNotification(remoteMessage.getNotification().getBody(),remoteMessage.getNotification().getTitle());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("appSample", "Token: " + s);
    }

    private void createNotification(String messageBody, String messageTitle) {

    }
}
