package com.craft.apps.countdowns.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.app.NotificationCompat.Builder;
import android.util.Log;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.util.IntentUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

// TODO: 7/2/17 Refactor and kill

/**
 * @author willie
 * @version 1.0.0
 * @since 5/29/17
 */
public class CountdownMessagingService extends FirebaseMessagingService {

    private static final String TAG = CountdownMessagingService.class.getSimpleName();

    private static final int RC_SHOW_COUNTDOWN_DETAILS = 6;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        super.onMessageReceived(remoteMessage);
        Log.d(TAG, "onMessageReceived: Message from " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "onMessageReceived: Notification body:" + remoteMessage.getNotification()
                    .getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        } else {
            Log.w(TAG, "onMessageReceived: Countdown payload doesn't exist!");
        }
        sendReminderNotification(remoteMessage);
    }

    private void sendNotification() {

    }

    private void sendReminderNotification(RemoteMessage message) {
        Map<String, String> data = message.getData();
        Intent intent = new Intent(IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent countdownIntent = PendingIntent.getActivity(this,
                RC_SHOW_COUNTDOWN_DETAILS, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder builder = new Builder(this);
        builder.setContentIntent(countdownIntent)
                .setSmallIcon(R.drawable.app_icon_large);
        if (data.size() > 0) {
            String name = data.get("countdownName");
            String description = data.get("countdownDescription");
            String daysUntil = data.get("countdownDaysUntil");
            builder.setContentTitle(daysUntil + " days until " + name);
            builder.setContentText(description);
        } else {
            builder.setContentTitle("Something days until")
                    .setContentText("Remember to do something.");
        }

        NotificationManager manager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);

        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            String defaultChannelId = getString(R.string.channel_countdown_updates_id);
            int importance = NotificationManagerCompat.IMPORTANCE_DEFAULT;
            NotificationChannel defaultChannel = new NotificationChannel(defaultChannelId,
                    getString(R.string.channel_countdown_updates_name), importance);
            manager.createNotificationChannel(defaultChannel);
            builder.setChannel(defaultChannelId);
        }
        int notifyId = 1;
        manager.notify(notifyId, builder.build());

    }
}
