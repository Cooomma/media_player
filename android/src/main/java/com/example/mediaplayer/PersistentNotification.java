package com.example.mediaplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.graphics.Bitmap;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.BitmapCallback;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.MediaDescriptionAdapter;
import com.google.android.exoplayer2.ui.PlayerNotificationManager.NotificationListener;

import static com.example.mediaplayer.C.PLAYBACK_CHANNEL_ID;
import static com.example.mediaplayer.C.PLAYBACK_NOTIFICATION_ID;

class PersistentNotification {
    private Service service;
    private PlayerNotificationManager playerNotificationManager;
    private MediaSessionCompat mediaSession;
    private MediaSessionConnector mediaSessionConnector;
    private VideoPlayer vplayer;

    PersistentNotification(Service service, VideoPlayer player) {
        this.vplayer = player;
        this.service = service;
    }

    static void create(Service service, VideoPlayer player) {
        PersistentNotification n = new PersistentNotification(service, player);
        n.createNotification();

    }

    void createNotification() {
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(service,
                PLAYBACK_CHANNEL_ID, R.string.playback_channel_name, PLAYBACK_NOTIFICATION_ID,
                new MyMediaDescriptionAdapter());

        playerNotificationManager.setNotificationListener(new MyNotificationListener());
        playerNotificationManager.setPlayer(vplayer.getPlayer());
    }

    private class MyMediaDescriptionAdapter implements MediaDescriptionAdapter {

        @Override
        public String getCurrentContentTitle(Player player) {
            return (String) vplayer.getCurrentPlayingSourceDetails().get("title");
        }

        @Nullable
        @Override
        public PendingIntent createCurrentContentIntent(Player player) {
            return null;
        }

        @Nullable
        @Override
        public String getCurrentContentText(Player player) {
            return (String) vplayer.getCurrentPlayingSourceDetails().get("desc");

        }

        @Nullable
        @Override
        public Bitmap getCurrentLargeIcon(Player player, BitmapCallback callback) {
            return null;
        }
    }

    class MyNotificationListener implements NotificationListener {

        @Override
        public void onNotificationStarted(int notificationId, Notification notification) {
            service.startForeground(notificationId, notification);
        }

        @Override
        public void onNotificationCancelled(int notificationId) {
            service.stopSelf();
        }
    }
}