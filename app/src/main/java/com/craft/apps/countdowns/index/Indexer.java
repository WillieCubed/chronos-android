package com.craft.apps.countdowns.index;

import android.util.Log;
import com.craft.apps.countdowns.common.model.Countdown;
import com.google.firebase.appindexing.FirebaseAppIndex;
import com.google.firebase.appindexing.Indexable;
import com.google.firebase.appindexing.builders.DigitalDocumentBuilder;
import com.google.firebase.appindexing.builders.Indexables;
import com.google.firebase.appindexing.builders.PersonBuilder;
import com.google.firebase.auth.FirebaseUser;
import java.util.Date;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (7/2/17)
 */
public final class Indexer {

    private static final String TAG = Indexer.class.getSimpleName();

    public static void indexCountdown(Countdown countdown, String countdownId, FirebaseUser user) {
        Log.d(TAG, "indexCountdown: Indexing countdown " + countdownId + " for user "
                + user.getUid());
        DigitalDocumentBuilder builder = Indexables.noteDigitalDocumentBuilder()
                .setUrl(getCountdownLink(countdownId))
                .setName(countdown.getTitle())
                .setText(generateCountdownLabel(countdown))
                .setDescription(countdown.getDescription())
                // TODO: 7/2/17 Fetch date using locale
                .setDateCreated(new Date(countdown.getStartTime()));
        if (user != null) {
            PersonBuilder personBuilder = Indexables.personBuilder()
                    .setIsSelf(true);
            if (user.getDisplayName() != null) {
                personBuilder.setName(user.getDisplayName());
            }
            if (user.getPhotoUrl() != null) {
                personBuilder.setImage(user.getPhotoUrl().toString());
            }
            builder.setAuthor(personBuilder);
        }
        Indexable indexable = builder.build();
        FirebaseAppIndex.getInstance().update(indexable);
    }

    public static String getCountdownLink(String countdownId) {
        // TODO: 7/2/17 Standardize this
        return "craft-countdown.firebasepp.com/countdown/" + countdownId;
    }

    public static void removeCountdownIndex(String countdownId) {
        FirebaseAppIndex.getInstance().remove(getCountdownLink(countdownId));
    }

    /**
     * Should be called when the user signs out
     * TODO: Automatically call this upon abstracted Users.signOut()
     */
    public static void removeIndexes() {
        FirebaseAppIndex.getInstance().removeAll();
    }

    private static String generateCountdownLabel(Countdown countdown) {
        // TODO: 3/15/17 Refactor this into proper place
//        DateTime dateTime = countdown.toDateTime();
//        String month = String.valueOf(dateTime.getMonthOfYear());
//        String dayOfMonth = String.valueOf(dateTime.getDayOfMonth());
//        String year = String.valueOf(dateTime.getYear());
//        String dateString = month + "-" + dayOfMonth + "-" + year;
//        return "Countdown to " + countdown.getTitle() + " on " + dateString;
        return countdown.getTitle();
    }
}
