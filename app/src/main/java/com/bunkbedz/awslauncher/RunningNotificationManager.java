/*  AwsLauncher
    Copyright (C) 2020  Amit Zohar
    For contact: amitzohar42@gmail.com

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.*/
package com.bunkbedz.awslauncher;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

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
