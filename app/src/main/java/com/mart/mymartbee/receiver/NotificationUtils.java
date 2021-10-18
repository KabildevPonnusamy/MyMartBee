package com.mart.mymartbee.receiver;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.mart.mymartbee.constants.Constants;
import com.mart.mymartbee.R;

import java.util.List;

public class NotificationUtils implements Constants {

    private static final String TAG = "appSample";

    private Context mContext;
 
    public NotificationUtils(Context mContext) {
        this.mContext = mContext;
    }
 
    private void showSmallNotification(NotificationCompat.Builder mBuilder, int icon, String title,
                                       String message, PendingIntent resultPendingIntent, Uri alarmSound) {
        Log.e(TAG, "showSmallNotification");
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
 
        inboxStyle.addLine(message);
 
        Notification notification;
        notification = mBuilder
                .setSmallIcon(icon).setTicker(title).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentIntent(resultPendingIntent)
                .setSound(alarmSound)
                .setStyle(inboxStyle)
                //.setWhen(getTimeMilliSec(timeStamp))
                .setSmallIcon(getNotificationIcon())
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(mContext.getResources().getColor(R.color.main_color), 1000, 1000)
                .setContentText(message)
                .build();
 
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
                    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.appicon_temp : R.drawable.appicon_temp;
                }

    /**
     * Method checks if the app is in background or not
     */
    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            }
                        }
                    }
                }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
                        }
                    }
 
        return isInBackground;
                }
 
    // Clears notification tray messages
    public static void clearNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}