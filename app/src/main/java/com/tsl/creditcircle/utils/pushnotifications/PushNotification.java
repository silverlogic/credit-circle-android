package com.tsl.creditcircle.utils.pushnotifications;

import java.util.Map;

/**
 * Created by kevinlavi on 11/17/17.
 */

public class PushNotification {

    private Map<String, String> hashMap;
    public static final String NOTIFICATION_GROUP = "notificationGroup";

    public PushNotification(Map<String, String> hashMap) {
        this.hashMap = hashMap;
    }
//
//    public void handle(Context context){
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
//        int pushId;
//        int taskId;
//        String message = hashMap.get("message");
//        String taskIdString = hashMap.get("task_id");
//        String pushIdString = hashMap.get("push_id");
//
//        if (pushIdString != null){
//            pushId = Integer.parseInt(pushIdString);
//        }
//        else pushId = new Random().nextInt();
//        if (taskIdString != null){
//            taskId = Integer.parseInt(taskIdString);
//        }
//        else taskId = 0;
//
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(context.getString(R.string.app_name))
//                .setGroup(NOTIFICATION_GROUP)
//                .setContentText(message)
//                .setAutoCancel(true);
//
//        if (taskId != 0) {
//            Intent resultIntent = new Intent(context, TaskDetailsActivity.class);
//            resultIntent.putExtra(TaskDetailsActivity.TASK_ID, taskId);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, pushId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            mBuilder.setContentIntent(pendingIntent);
//            downloadTaskDetail(taskId);
//        }
//        else {
//            // no task. go to map
//            Intent resultIntent = new Intent(context, MainActivity.class);
//            resultIntent.putExtra(MainActivity.PUSH_MESSAGE, message);
//            PendingIntent pendingIntent = PendingIntent.getActivity(context, pushId, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            mBuilder.setContentIntent(pendingIntent);
//        }
//        notificationManager.notify(pushId, mBuilder.build());
//        boolean hasGroupSummary = Hawk.get(Constants.NOTIFICATION_HAS_SUMMARY, false);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !hasGroupSummary) {
//            notificationManager.notify(1, getNotificationSummary(context));
//        }
//    }
//
//    private Notification getNotificationSummary(Context context){
//        Hawk.put(Constants.NOTIFICATION_HAS_SUMMARY, true);
//        return new NotificationCompat.Builder(context)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle(context.getString(R.string.app_name))
//                .setGroup(NOTIFICATION_GROUP)
//                .setGroupSummary(true)
//                .setContentText(context.getString(R.string.app_name))
//                .setAutoCancel(true)
//                .setDeleteIntent(getDeleteIntent(context))
//                .build();
//    }
//
//    private PendingIntent getDeleteIntent(Context context){
//        Intent intent = new Intent(context, DeletePushReceiver.class);
//        return PendingIntent.getBroadcast(context, 1, intent, 0);
//    }
//
//    public static class DeletePushReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Timber.d("Delete push -----");
//            Hawk.put(Constants.NOTIFICATION_HAS_SUMMARY, false);
//        }
//    }
//
//    private void downloadTaskDetail(Integer taskId) {
//        // Download the task info to have everything up to date
//        BaseApi api = new BaseApiManager().getAppApi();
//        api.getTaskDetail(Constants.getToken(), taskId)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Subscriber<Task>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(Task task) {
//                        Writer.persist(task);
//                    }
//                });
//    }
}
