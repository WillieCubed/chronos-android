package com.craft.apps.countdowns.notification;

import android.app.IntentService;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

// TODO: 6/24/17 Unify this and the CountdownMessagingService

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * @author willie
 * @version 1.0.0
 * @since 6/23/17
 */
public class CountdownNotificationService extends FirebaseMessagingService {


    /**
     * Required public empty constructor
     */
    public CountdownNotificationService() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            // Initialize this in case the app hasn't been launched yet
            NotificationSender.initializeChannels(this);
        }
        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
    }
}
