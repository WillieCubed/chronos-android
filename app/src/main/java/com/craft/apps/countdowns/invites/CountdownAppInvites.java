package com.craft.apps.countdowns.invites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * @version 1.1.0
 * @since 1.1.0
 */
public class CountdownAppInvites {

    private static final String TAG = CountdownAppInvites.class.getSimpleName();

    /**
     * Determines if the given {@link Intent} was launched from a Firebase App Invites link
     * @deprecated Use {@link #handleAppInvite(Intent)}
     */
    // TODO: 7/1/17 Implement me
    @Deprecated
    public static void handleInvite(Activity activity, Intent intent) {
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent)
                .addOnSuccessListener(activity, data -> {
                    if (data == null) {
                        Log.d(TAG, "getInvitation: no data");
                        return;
                    }

                    // Get the deep link
                    Uri deepLink = data.getLink();

                    // Extract invite
                    FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
                    if (invite != null) {
                        String invitationId = invite.getInvitationId();
                    }
                })
                .addOnFailureListener(activity, e -> {
                    Log.w(TAG, "handleInvite: Error when fetching app invite data", e);
                });
    }

    public static Task<PendingDynamicLinkData> handleAppInvite(Intent intent) {
        return FirebaseDynamicLinks.getInstance().getDynamicLink(intent);
    }
}
