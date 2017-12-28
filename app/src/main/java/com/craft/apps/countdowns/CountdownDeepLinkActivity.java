package com.craft.apps.countdowns;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.model.Countdown;

import static com.craft.apps.countdowns.common.util.IntentUtils.ARG_COUNTDOWN_ID;

/**
 * An {@link android.app.Activity} that displays {@link Countdown} details from sources outside the
 * app.
 *
 * @version 1.0.0
 * @see CountdownModalDetailFragment
 * @since 1.0.0
 */
public class CountdownDeepLinkActivity extends AppCompatActivity {

    private static final String TAG = CountdownDeepLinkActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_deep_link);

        handleIntent(getIntent());
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.v(TAG, "onNewIntent: " + intent.getAction());
        handleIntent(intent);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        ((CountdownDetailDisplay) getSupportFragmentManager()
//                .findFragmentByTag("CountdownDetailFragment"))
//                .dismissDisplay();
//        finish();
//    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        Log.d(TAG, "handleIntent: Intent action is " + action);
        String data = intent.getDataString();
        Log.d(TAG, "handleIntent: Intent data is " + data);
        Bundle args = intent.getExtras();

        String countdownId;
        if (args.containsKey(ARG_COUNTDOWN_ID)) {
            countdownId = args.getString(ARG_COUNTDOWN_ID);
        } else if (Intent.ACTION_VIEW.equals(action) && data != null) {
            countdownId = data.substring(data.lastIndexOf("/") + 1);
            Log.d(TAG, "handleIntent: Countdown ID is " + countdownId);
//            Uri contentUri = Uri.parse("content://com.craft.apps.countdowns/countdown/").buildUpon()
//                    .appendPath(countdownId).build();
        } else {
            throw new IllegalStateException("Countdown ID must be supplied in intent or data!");
        }
        Log.d(TAG, "handleIntent: Showing countdown details for ID " + countdownId);
        showCountdownFragment(countdownId);
    }

    private void showCountdownFragment(String countdownId) {
        CountdownAnalytics.getInstance(this).logSingleWidgetEngagement();
        CountdownDetailDisplay display = CountdownModalDetailFragment.newInstance(countdownId);
        display.showDisplay(getSupportFragmentManager());
    }
}
