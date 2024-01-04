package com.craft.apps.countdowns.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import android.util.Log;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.util.IntentUtils;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Map;

// TODO: 7/2/17 Refactor and kill

/**
 * @author willie
 * @version 1.0.0
 * @since 5/29/17
 */
public class CountdownNotificationService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String refreshedToken) {
        super.onNewToken(refreshedToken);
        Log.d(TAG, "onTokenRefresh: Attempting to persist new token " + refreshedToken
                + " to database");
        sendRegistrationToServer(refreshedToken);
    }

    private static final String TAG = CountdownNotificationService.class.getSimpleName();

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
                RC_SHOW_COUNTDOWN_DETAILS, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.channel_countdown_updates_id));
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

        String defaultChannelId = getString(R.string.channel_countdown_updates_id);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel defaultChannel = new NotificationChannel(defaultChannelId,
                getString(R.string.channel_countdown_updates_name), importance);
        manager.createNotificationChannel(defaultChannel);
        builder.setChannelId(defaultChannelId);
        int notifyId = 1;
        manager.notify(notifyId, builder.build());

    }

    /**
     * Persists an updated instance ID token to the signed in user's Firebase OldDatabase reference
     *
     * @param token A new token to persist
     */
    private static void sendRegistrationToServer(String token) {
        FirebaseUser user = Users.getCurentUser();
        if (user != null) {
            OldDatabase.getUserReference(user.getUid()).child(OldDatabase.PATH_FCM_TOKENS)
                    .child(token).setValue(true, (databaseError, databaseReference) -> {
                        if (databaseError != null) {
                            Log.e(TAG, "onComplete: Couldn't update user FCM token; " +
                                    databaseError.getDetails(), databaseError.toException());
                        } else {
                            Log.i(TAG, "onComplete: Successfully updated user FCM token.");
                        }
                    });
        }
    }
}
