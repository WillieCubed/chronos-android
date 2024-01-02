package com.craft.apps.countdowns.common.essentials;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.craft.apps.countdowns.R;

/**
 * An {@link android.app.Activity} that allows a user to send a message to an given backend.
 *
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (5/7/17)
 */
public class FeedbackActivity extends AppCompatActivity {

    /**
     * Starts a new {@code FeedbackActivity} instance.
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, FeedbackActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * A callback that is notified when a user submits feedback in a {@link FeedbackActivity}
     */
    public interface FeedbackCallback {

        /**
         * Notifies this callback that
         * @param email
         * @param content A non-null
         */
        void sendFeedback(@NonNull String email, @NonNull String content);
    }
}
