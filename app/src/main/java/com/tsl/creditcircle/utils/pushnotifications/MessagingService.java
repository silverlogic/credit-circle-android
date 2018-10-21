package com.tsl.creditcircle.utils.pushnotifications;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

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
            String id = remoteMessage.getData().get("id");
            String name = remoteMessage.getData().get("name");


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Vouch Request")
                            .setContentText(name + " has requested for you to vouch for her");
            Intent notificationIntent = new Intent(this, InvestActivity.class);
            notificationIntent.putExtra("id", Integer.parseInt(id));
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(contentIntent);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// notificationID allows you to update the notification later on.
            mNotificationManager.notify(01, mBuilder.build());
        }
    }
}
