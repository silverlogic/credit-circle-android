package com.tsl.creditcircle.utils.pushnotifications;

/**
 * Created by kevinlavi on 11/17/17.
 */

import com.orhanobut.hawk.Hawk;
import com.tsl.creditcircle.api.BaseApi;
import com.tsl.creditcircle.api.BaseApiManager;
import com.tsl.creditcircle.utils.Constants;

import org.parceler.Parcel;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * Store the info of the current fcm token.
 *
 * It also have methods to upload a new token to the server.
 */

@Parcel
public class FCMToken {
    String fcmToken;

    /**
     * Store and upload a FCM Token.
     * @param fcmToken the token received from Firebase
     */
    public static void storeAndUploadNewToken(String fcmToken) {
        FCMToken token = new FCMToken(fcmToken);
        Hawk.put(Constants.FCM_TOKEN, token);
        token.uploadToServer();
    }

    /**
     * Check if an existing fcm token exists and if it exists and hasn't been uploaded yet it is sent to the server.
     */
    public static void uploadStoredTokenIfNeeded() {
        FCMToken fcmToken = Hawk.get(Constants.FCM_TOKEN, null);
        Timber.d("TESTING 1");
        if (fcmToken != null) {
            Timber.d("TESTING 2 = " + fcmToken.fcmToken);
            fcmToken.uploadToServer();
        }
    }

    public static void setNeedsRefresh() {
        FCMToken fcmToken = Hawk.get(Constants.FCM_TOKEN, null);
        if (fcmToken != null) {
            Hawk.put(Constants.FCM_TOKEN, fcmToken);
        }
    }

    /**
     * Empty constructor ned for parcelable
     */
    FCMToken() {
    }

    private FCMToken(String token) {
        this.fcmToken = token;
    }

    private void uploadToServer() {

        final BaseApi api = new BaseApiManager().getAppApi();
        final String token = Constants.getToken();
        FCMDeviceBody body = new FCMDeviceBody(fcmToken);
        Timber.e("TESTING 3" + body.toString());
        api.uploadFCMToken(token, body)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .onErrorResumeNext(
                        api.updateFCMToken(token, fcmToken, body)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribeOn(Schedulers.io())
                )
                .subscribe(new PromiseSubscriber<Void>() {
                    @Override
                    protected void then(Void value) {
                        Hawk.put(Constants.FCM_TOKEN, FCMToken.this);
                    }

                    @Override
                    protected void error(Throwable e) {
                        updateTokenToServer();
                    }
                });
    }

    private void updateTokenToServer() {

    }
}
