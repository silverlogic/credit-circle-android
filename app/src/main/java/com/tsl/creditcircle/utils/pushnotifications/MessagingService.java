package com.tsl.creditcircle.utils.pushnotifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import timber.log.Timber;

/**
 * Created by kevinlavi on 11/17/17.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("PUSH CHECK = " + remoteMessage.getData().toString());
        // Create the push
        PushNotification pushNotification = new PushNotification(remoteMessage.getData());

        // handle
        //pushNotification.handle(this);
    }
}
