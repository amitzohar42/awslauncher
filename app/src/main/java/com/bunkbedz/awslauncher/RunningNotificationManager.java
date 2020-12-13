package com.bunkbedz.awslauncher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import java.net.ContentHandler;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class RunningNotificationManager {
    public static final int NOTIFICATION_ID = 5018;

    private final Context context;

    public RunningNotificationManager(Context context) {
        this.context = context;
    }

    public void createNotificationChannel() {
        String channelId = context.getString(R.string.notification_channel_id);
        String name = context.getString(R.string.notification_channel_name);
        String description = context.getString(R.string.notification_channel_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(channelId, name, importance);
        channel.setDescription(description);
        NotificationManager  notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void showNotification() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(NOTIFICATION_ID, getNotification());
    }

    public void cancelNotification() {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    private Notification getNotification() {
        return new NotificationCompat.Builder(context, context.getString(R.string.notification_channel_id))
                .setOngoing(true)
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentText(context.getString(R.string.notification_text))
                .setSmallIcon(R.drawable.ic_server)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
    }
}
