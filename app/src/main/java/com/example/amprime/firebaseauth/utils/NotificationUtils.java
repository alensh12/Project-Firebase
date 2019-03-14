package com.example.amprime.firebaseauth.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.text.TextUtils;

import com.example.amprime.firebaseauth.R;

/**
 * Created by amprime on 10/29/17.
 */

public class NotificationUtils {
    private Context context;
    private String title;
    private String message;
    private String timeStamp;
    Intent intent;

    public NotificationUtils() {

    }
    public NotificationUtils(Context context) {
        this.context = context;
    }

    public void showNotificationMessage(String title,String message, String timeStamp,Intent intent){
       /**Check for Empty Push Message**/
        if(TextUtils.isEmpty(message)){
            return;
        }
       final int icon  = R.mipmap.ic_launcher;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent pendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        final Notification.Builder builder = new Notification.Builder(context);

        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+
                "://"+context.getPackageName()+"/raw/notification");
        showSmallNotification(builder,icon,alarmSound,title,message,timeStamp,pendingIntent);
    }

    private void showSmallNotification(
            Notification.Builder builder,
            int icon,
            Uri alarmSound,
            String title,
            String message,
            String timeStamp,
            PendingIntent pendingIntent) {

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);

        Notification notification;
        notification = builder.setSmallIcon(icon)
                .setTicker(title)
                .setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(title)
                .build();
        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(100,notification);
    }


}
