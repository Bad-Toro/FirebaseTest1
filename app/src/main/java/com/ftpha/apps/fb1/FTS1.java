package com.ftpha.apps.fb1;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Fernando on 5/24/2016.
 */
public class FTS1 extends FirebaseMessagingService{



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        Log.d("FT747   ", "From: " + remoteMessage.getFrom());
        Log.d("FT747   ", "Notification Message Body: " + remoteMessage.getNotification().getBody());
    }


}
