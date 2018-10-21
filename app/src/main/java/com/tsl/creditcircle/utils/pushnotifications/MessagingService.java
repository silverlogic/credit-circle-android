package com.tsl.creditcircle.utils.pushnotifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.tsl.creditcircle.R;
import com.tsl.creditcircle.invest.InvestActivity;
import com.tsl.creditcircle.model.event.VouchEvent;
import com.tsl.creditcircle.model.objects.Friend;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by kevinlavi on 11/17/17.
 */

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Timber.d("PUSH CHECK = " + remoteMessage.getData().toString());

        String type = remoteMessage.getData().get("type");
        if (type.equalsIgnoreCase("accepted")){
            Timber.d("PUSH CHECK = 1" );
            String user = remoteMessage.getData().get("vouching_user");
            try {
                JSONObject obj = new JSONObject(user);
                int vouched = Integer.parseInt(remoteMessage.getData().get("vouch_amount"));
                Friend friend = new Friend(obj.getInt("id"), obj.getString("name"), obj.getString("job"), obj.getInt("stars"), vouched);
                EventBus.getDefault().post(new VouchEvent(friend,
                        vouched,
                        Integer.parseInt(remoteMessage.getData().get("investment_amount"))));
            } catch (Throwable t) {
            }
        }
        else {
            Timber.d("PUSH CHECK = 2" );
            String id = remoteMessage.getData().get("id");
            String name = remoteMessage.getData().get("name");

//                            .setContentTitle("Vouch Request")
//                    .setContentText(name + " has requested for you to vouch for her");

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
                Intent notificationIntent = new Intent(this, InvestActivity.class);
                notificationIntent.putExtra("id", Integer.parseInt(id));
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                Notification notification = new Notification.Builder(this, id)
                        .setContentTitle("Vouch Request")
                        .setContentText(name +" has requested for you to vouch for her")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setSubText("SubText")
                        .setContentIntent(contentIntent)
                        //.addExtras(new Bundle())
                        .build();
                notificationManager.notify(Integer.parseInt(id), notification);
            } else {
                Notification notification = new Notification.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Vouch Request")
                        .setContentText(name +" has requested for you to vouch for her")
                        .setPriority(Notification.PRIORITY_HIGH)
                        .build();
                notificationManager.notify(Integer.parseInt(id), notification);
            }
        }
    }
}
