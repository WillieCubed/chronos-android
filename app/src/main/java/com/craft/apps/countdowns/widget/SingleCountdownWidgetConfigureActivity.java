package com.craft.apps.countdowns.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.craft.apps.countdowns.CountdownCreationActivity;
import com.craft.apps.countdowns.CountdownListFragment;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.adapter.CountdownRecyclerAdapter.CountdownSelectionListener;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;

/**
 * The configuration screen for a {@link SingleCountdownWidget}.
 *
 * @author willie
 * @version 1.0.0
 * @since 3/18/17
 */
public class SingleCountdownWidgetConfigureActivity extends AppCompatActivity implements
        View.OnClickListener,
        CountdownSelectionListener {

    private static final String TAG = SingleCountdownWidgetConfigureActivity.class.getSimpleName();

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.activity_configure_countdown_widget);
        FirebaseUser user = Users.getCurentUser();
        if (user == null) {
            finish();
            Toast.makeText(this, R.string.sign_in, Toast.LENGTH_SHORT).show();
            return;
        }
        Fragment fragment = CountdownListFragment.newInstance(user.getUid());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_countdown_list, fragment, "CountdownListFragment")
                .commit();

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Log.v(TAG, "onCreate: Received intent: " + intent);
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            Log.w(TAG, "onCreate: Launched with invalid app widget ID:" + mAppWidgetId);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create_countdown:
                Intent intent = new Intent(this, CountdownCreationActivity.class);
                startActivityForResult(intent, WidgetManager.RC_WIDGET_ADD);
                break;
        }
    }

    @Override
    public void onLoad() {
        hideProgressBar();
    }

    @Override
    public void onCountdownSelected(String countdownId) {
        WidgetManager.saveCountdownIdPreference(this, mAppWidgetId, countdownId);
        // It is the responsibility of the configuration activity to update the app widget
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        SingleCountdownWidget.updateAppWidget(this, appWidgetManager, mAppWidgetId);

        // Make sure we pass back the original appWidgetId
        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onCountdownLongSelected(String countdownId) {
        // We don't want to do anything fancy here
    }


    private void hideProgressBar() {
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }
}

