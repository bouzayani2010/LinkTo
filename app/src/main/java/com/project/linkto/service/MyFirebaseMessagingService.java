package com.project.linkto.service;


import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.
        Log.i(TAG, "From: " + remoteMessage.getFrom());
        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Toast.makeText(this,"From: " + remoteMessage.getFrom()+"Notification Message Body: "
                + remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();
        displayBadge(1);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();

        displayBadge(0);
    }

    private void displayBadge(int n) {
        if (n > 0) {
            int badgeCount = n;
            ShortcutBadger.applyCount(this, badgeCount);
        } else
            ShortcutBadger.removeCount(this);
    }
}
