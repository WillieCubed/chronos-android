package com.craft.apps.countdowns.invites;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.1.0 (7/1/17)
 */
public class CountdownAppInvites {

    private static final String TAG = CountdownAppInvites.class.getSimpleName();

    /**
     * Determines if the given {@link Intent} was launched from a Firebase App Invites link
     */
    // TODO: 7/1/17 Implement me
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

                    // Handle the deep link
                    // ...
                })
                .addOnFailureListener(activity, e -> {
                    Log.w(TAG, "handleInvite: Error when fetching app invite data", e);
                });
    }
}
