package com.craft.apps.countdowns.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.craft.apps.countdowns.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author willie
 * @version 1.0.0
 * @since 6/23/17
 */
public class NotificationSender {

    public static final int NOTIFICATION_ID_DEFAULT = 1;

    public static final String CHANNEL_APP_UPDATES = "channel_app_updates";

    public static final String CHANNEL_COUNTDOWN_UPDATES = "channel_countdown_updates";

    private final Context mContext;

    private final NotificationManager mNotificationManager;

    NotificationSender(Context context) {
        mContext = context;
        mNotificationManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
    }

    public static void initializeChannels(Context context) {
        List<NotificationChannel> channels = new ArrayList<>();

        // Create the countdown updates channel
        String name = context.getString(R.string.channel_countdown_updates_name);
        String description = context.getString(R.string.channel_countdown_updates_description);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel countdownUpdatesChannel = new NotificationChannel(
                CHANNEL_COUNTDOWN_UPDATES, name, importance);
        countdownUpdatesChannel.setDescription(description);
        countdownUpdatesChannel.setShowBadge(true);
        channels.add(countdownUpdatesChannel);

        // Create the app updates channel
        String updatesName = context.getString(R.string.channel_app_updates_name);
        String updatesDescription = context.getString(R.string.channel_app_updates_description);
        int updatesImportance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel appUpdatesChannel = new NotificationChannel(CHANNEL_APP_UPDATES,
                updatesName, updatesImportance);
        appUpdatesChannel.setDescription(updatesDescription);
        appUpdatesChannel.setShowBadge(true);
        channels.add(appUpdatesChannel);

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannels(channels);
    }

    public Notification createNotification(String title, String content, String channelId) {
        return new Notification.Builder(mContext, channelId)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_timelapse_red_500_24dp)
                .build();
    }

    public void sendNotification(Notification notification) {
        mNotificationManager.notify(NOTIFICATION_ID_DEFAULT, notification);
    }

    public void createNotificationChannelGroup() {
        // TODO: 6/24/17 Allow multiple channels for user-defined countdown groups
    }

    public boolean hasCreatedChannel(String channelId) {
        return mNotificationManager.getNotificationChannel(channelId) != null;
    }

    public void createNotificationChannel() {
        String name = mContext.getString(R.string.channel_app_updates_name);
        String description = mContext.getString(R.string.channel_app_updates_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel appUpdatesChannel = new NotificationChannel(CHANNEL_APP_UPDATES, name,
                importance);
        appUpdatesChannel.setDescription(description);
        appUpdatesChannel.setShowBadge(true);
        mNotificationManager.createNotificationChannel(appUpdatesChannel);
    }

    public void startExtraSettingsIntent() {
        Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            intent.putExtra(Settings.EXTRA_CHANNEL_ID, mChannel.getId());
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, mContext.getPackageName());
        mContext.startActivity(intent);

    }
}
