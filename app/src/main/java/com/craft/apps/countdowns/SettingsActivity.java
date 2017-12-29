package com.craft.apps.countdowns;

import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.Log;
import android.widget.Toast;

import com.craft.apps.countdowns.common.database.CountdownManager;
import com.craft.apps.countdowns.common.settings.Preferences;
import com.craft.apps.countdowns.index.Indexer;
import com.craft.apps.countdowns.purchase.Biller;
import com.craft.apps.countdowns.util.Users;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

/**
 * An {@link AppCompatActivity} that allows the user to manipulate local settings that will be
 * persisted to disk with the {@link PreferenceManager} using a {@link Preferences}
 *
 * @version 1.0.0
 * @since 1.0.0
 */
public class SettingsActivity extends AppCompatActivity implements
        OnPreferenceChangeListener {

    public static final String ACTION_RESET_APP = "com.craft.apps.countdowns.action.ACTION_RESET_APP";
    private static final String TAG = SettingsActivity.class.getSimpleName();

    private static final int RC_PERMISSION_WRITE_CALENDAR = 2001;

    /**
     * Starts a new SettingsActivity instance
     *
     * @param context Any valid context
     */
    public static void start(Context context) {
        Intent starter = new Intent(context, SettingsActivity.class);
        context.startActivity(starter);
    }

    /**
     * Initializes the activity's layout and shows a dialog if the action is
     * {@link SettingsActivity#ACTION_RESET_APP}
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        if (ACTION_RESET_APP.equals(getIntent().getAction())) {
            Log.d(TAG, "onCreate: App reset action requested");
            showResetDialog();
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        // TODO: 6/23/17 Use if statement
        String key = preference.getKey();
        Log.d(TAG, "onPreferenceChange: Changed preference key is " + key);
        switch (key) {
            case "pref_syncSystemCalendar":
                if ((boolean) newValue) {
                    checkCalendarPermissions();
                }
                break;
            case "pref_countdownDisplayUnits":
                // TODO: 6/24/17 Change summary based on selection
                String[] values = getResources()
                        .getStringArray(R.array.pref_countdownUnitType_values);
                String[] summaries = getResources()
                        .getStringArray(R.array.pref_countdownUnitType_labels);
                for (int i = 0; i < values.length; i++) {
                    if (values[i].equals(newValue)) {
                        preference.setSummary(summaries[i]);
                        break;
                    }
                }
                break;
        }
        return true;
    }

    private void showResetDialog() {
        AlertDialog dialog = new Builder(this)
                .setCancelable(false)
                .setTitle(R.string.query_reset_app)
                .setMessage(getString(R.string.query_reset_app_details))
                .setPositiveButton(android.R.string.yes, (dialog1, which) -> {
                    Log.d(TAG, "onClick: Resetting app state.");
                    dialog1.dismiss();
                    Preferences.getInstance(this).resetPreferences();
                    FirebaseUser user = Users.getCurentUser();
                    Users.signOut(this)
                            .addOnSuccessListener(this, aVoid -> {
                                // TODO: 7/2/17 Fix finish()
                                // TODO: 7/2/17 Add more granular controls
                                CountdownManager.getUserCountdownsReference(user.getUid())
                                        .setValue(null);
                                Indexer.removeIndexes();
                                StartActivity.start(SettingsActivity.this);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Log.w(TAG, "onFailure: Error resetting app", e);
                                Toast.makeText(SettingsActivity.this,
                                        "Please try again",
                                        Toast.LENGTH_SHORT)
                                        .show();
                            });

                })
                .setNegativeButton(android.R.string.no, (dialog2, which) -> {
                    Log.d(TAG, "onClick: Cancelling app reset.");
                    dialog2.cancel();
                })
                .create();
        dialog.show();
    }

    private void checkCalendarPermissions() {
        int permissionsGranted = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_CALENDAR);
        if (permissionsGranted != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permission.WRITE_CALENDAR)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[] {permission.WRITE_CALENDAR},
                        RC_PERMISSION_WRITE_CALENDAR);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_PERMISSION_WRITE_CALENDAR:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
        }
    }

    private void disableAds() {
        Log.d(TAG, "Disabling ads");
        Biller biller = new Biller(this);
        FirebaseUser user = Users.getCurentUser();
        biller.disableAds(user.getUid());
    }

    /**
     * A simple {@link Fragment} subclass that contains settings for this app's preferences
     */
    public static class SettingsFragment extends PreferenceFragmentCompat {

        private static final String TAG = SettingsFragment.class.getSimpleName();

        /**
         * Required empty public constructor
         */
        public SettingsFragment() {
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            Log.v(TAG, "Loading preferences from XML resource");
            addPreferencesFromResource(R.xml.app_preferences);

            Preference preference = findPreference("pref_root_key");
            preference.setOnPreferenceChangeListener((OnPreferenceChangeListener) getActivity());
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            String key = preference.getKey();
            Log.v(TAG, "onPreferenceTreeClick: Preference is '" + key + "'");
            if (Objects.equals(key, getString(R.string.pref_disableAds_key))) {
                ((SettingsActivity) getActivity()).disableAds();
            }
            return super.onPreferenceTreeClick(preference);
        }
    }
}
