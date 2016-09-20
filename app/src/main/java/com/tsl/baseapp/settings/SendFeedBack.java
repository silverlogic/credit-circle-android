package com.tsl.baseapp.settings;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.jaredrummler.android.device.DeviceName;
import com.tsl.baseapp.BuildConfig;
import com.tsl.baseapp.R;
import com.tsl.baseapp.model.objects.user.User1;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by kevinlavi on 5/4/16.
 */
public class SendFeedBack {
    public void send(User1 mUser1, Context mContext){
        String buildVersion = BuildConfig.VERSION_NAME;
        String firstName = mUser1.getFirst_name();
        String lastName = mUser1.getLast_name();
        String userId = String.valueOf(mUser1.getId());
        String deviceName = DeviceName.getDeviceName();
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        String appName = mContext.getString(R.string.app_name);
        String subject = appName + " | Feedback | " + currentDateTimeString;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"kl@tsl.io"});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT,
                "Dear " + appName + "," +
                        "\n" + "\n" + "\n" +
                        "Version: " + buildVersion + "\n" +
                        "User1 ID: " + userId + "\n" +
                        "First Name: " + firstName + "\n" +
                        "Last Name: " + lastName + "\n" +
                        "Device: " + deviceName);

        try {
            mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.send_mail)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(mContext, R.string.no_email_clients_installed, Toast.LENGTH_SHORT).show();
        }
    }
}
