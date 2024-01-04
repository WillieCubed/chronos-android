package com.craft.apps.countdowns.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.craft.apps.countdowns.R
import com.craft.apps.countdowns.common.database.OldDatabase
import com.craft.apps.countdowns.common.util.IntentUtils
import com.craft.apps.countdowns.util.Users
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * @author willie
 * @version 1.0.0
 * @since 5/29/17
 */
class CountdownNotificationService : FirebaseMessagingService() {
    override fun onNewToken(refreshedToken: String) {
        Log.d(
            TAG, "onTokenRefresh: Attempting to persist new token " + refreshedToken
                    + " to database"
        )
        sendRegistrationToServer(refreshedToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived: Message from " + remoteMessage.from)
        if (remoteMessage.notification != null) {
            Log.d(
                TAG, "onMessageReceived: Notification body:" + remoteMessage.notification!!
                    .body
            )
        }
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        } else {
            Log.w(TAG, "onMessageReceived: Countdown payload doesn't exist!")
        }
        sendReminderNotification(remoteMessage)
    }

    private fun sendNotification() {}

    private fun sendReminderNotification(message: RemoteMessage) {
        val data = message.data
        val intent = Intent(IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS)
            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val countdownIntent = PendingIntent.getActivity(
            this,
            RC_SHOW_COUNTDOWN_DETAILS,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        val builder =
            NotificationCompat.Builder(this, getString(R.string.channel_countdown_updates_id))
        builder.setContentIntent(countdownIntent)
            .setSmallIcon(R.drawable.app_icon_large)
        if (data.isNotEmpty()) {
            val name = data["countdownName"]
            val description = data["countdownDescription"]
            val daysUntil = data["countdownDaysUntil"]
            builder.setContentTitle("$daysUntil days until $name")
            builder.setContentText(description)
        } else {
            builder.setContentTitle("Something days until")
                .setContentText("Remember to do something.")
        }
        val manager = getSystemService(NotificationManager::class.java)
        val defaultChannelId = getString(R.string.channel_countdown_updates_id)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val defaultChannel = NotificationChannel(
            defaultChannelId,
            getString(R.string.channel_countdown_updates_name), importance
        )
        manager.createNotificationChannel(defaultChannel)
        builder.setChannelId(defaultChannelId)
        val notifyId = 1
        manager.notify(notifyId, builder.build())
    }

    companion object {
        private val TAG = CountdownNotificationService::class.java.simpleName
        private const val RC_SHOW_COUNTDOWN_DETAILS = 6

        /**
         * Persists an updated instance ID token to the signed in user's Firebase OldDatabase reference
         *
         * @param token A new token to persist
         */
        private fun sendRegistrationToServer(token: String) {
            val user = Users.getCurentUser()
            if (user != null) {
                OldDatabase.getUserReference(user.uid).child(OldDatabase.PATH_FCM_TOKENS)
                    .child(token)
                    .setValue(true) { databaseError: DatabaseError?, _: DatabaseReference? ->
                        if (databaseError != null) {
                            Log.e(
                                TAG, "onComplete: Couldn't update user FCM token; " +
                                        databaseError.details, databaseError.toException()
                            )
                        } else {
                            Log.i(TAG, "onComplete: Successfully updated user FCM token.")
                        }
                    }
            }
        }
    }
}
