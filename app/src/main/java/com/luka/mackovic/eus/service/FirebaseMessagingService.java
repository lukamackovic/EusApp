package com.luka.mackovic.eus.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.luka.mackovic.eus.R;
import com.luka.mackovic.eus.ui.activity.EventDetailsActivity_;
import com.luka.mackovic.eus.ui.activity.NewsDetailsActivity_;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String OPEN_NEWS_NOTIFICATION = "OPEN_NEWS_NOTIFICATION";
    private static final String OPEN_EVENT_NOTIFICATION = "OPEN_EVENT_NOTIFICATION";

    private static final String ID = "id";
    private static final String DEFAULT = "default";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody(),
                    remoteMessage.getData().get(ID),
                    remoteMessage.getNotification().getClickAction());
        }
    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    private void sendNotification(String title, String message, String id, String clickAction) {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, getIntent(id, clickAction), PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, DEFAULT)
                .setColor(getColor(R.color.colorPrimaryDark))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (notificationManager != null) {
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    private Intent getIntent(String id, String clickAction) {
        switch (clickAction) {
            case OPEN_EVENT_NOTIFICATION:
                Intent eventIntent = EventDetailsActivity_.intent(this).eventId(id).get();
                eventIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                return eventIntent;

            case OPEN_NEWS_NOTIFICATION:
                Intent articleIntent = NewsDetailsActivity_.intent(this).newsId(id).get();
                articleIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                return articleIntent;

            default:
                return null;
        }
    }
}