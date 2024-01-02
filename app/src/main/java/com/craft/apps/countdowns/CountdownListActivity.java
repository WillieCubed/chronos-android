package com.craft.apps.countdowns;

import android.app.assist.AssistContent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.craft.apps.countdowns.common.essentials.DrawerManager;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import androidx.fragment.app.Fragment;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.craft.apps.countdowns.SortOptionDialog.SelectionListener;
import com.craft.apps.countdowns.adapter.CountdownRecyclerAdapter.CountdownSelectionListener;
import com.craft.apps.countdowns.common.analytics.CountdownAnalytics;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.model.SortOptions.SortOption;
import com.craft.apps.countdowns.common.privilege.UserPrivileges;
import com.craft.apps.countdowns.common.settings.Preferences;
import com.craft.apps.countdowns.common.util.IntentUtils;
import com.craft.apps.countdowns.util.Users;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.getkeepsafe.taptargetview.TapTargetView.Listener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_FEATURE_DISCOVERY;
import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS;

import java.util.Arrays;
import java.util.List;

/**
 * An {@link android.app.Activity} that lists {@link Countdown}s using a {@link
 * CountdownListFragment}
 * <p> Currently, this is the entry point to the app and the primary screen
 * for interaction. Users can view countdown information, create new countdowns, and navigate to the
 * {@link SettingsActivity} from here.
 *
 * @author willie
 * @version 1.0.1
 * @see CountdownListFragment
 * @see CountdownCreationActivity
 * @see SettingsActivity
 * @since 3/18/17
 */
public class CountdownListActivity extends AppCompatActivity implements
        OnClickListener,
        CountdownSelectionListener,
        OnNavigationItemSelectedListener,
        ActionMode.Callback,
        SelectionListener {

    private static final String TAG = CountdownListActivity.class.getSimpleName();

    private static final int RC_APP_INVITE = 2;

    private ActionMode mActionMode;

    private FirebaseUser mUser;

    private DrawerManager mDrawerManager;

    private AdView mBannerAd;

    public static void start(Context context) {
        Intent starter = new Intent(context, CountdownListActivity.class);
        context.startActivity(starter);
    }

    public static void fromOnboarding(Context context) {
        Intent starter = new Intent(ACTION_FEATURE_DISCOVERY);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleApiAvailability.getInstance().makeGooglePlayServicesAvailable(this)
                .addOnCompleteListener(task -> {
                    if (task.getException() == null) {
                        Log.i(TAG, "onComplete: Device has Google Play Services");
                    } else {
                        Log.w(TAG, "onComplete: Device does not have Google Play Services");
                    }
                });

        if (!Preferences.getInstance(this).isOnboarded()) {
            OnboardingActivity.start(this);
            finish();
            return;
        }

        mUser = Users.getCurentUser();
        if (mUser == null) {
            StartActivity.start(this);
            finish();
            return;
        }

        setContentView(R.layout.activity_countdown_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerManager = new DrawerManager(this, this, toolbar,
                findViewById(R.id.drawer_layout));
        mDrawerManager.updateNavigationHeader(mUser.getDisplayName(), mUser.getEmail(),
                mUser.getPhotoUrl() != null ? mUser.getPhotoUrl().toString() : null);
        mDrawerManager.setAccountButtonListener(view -> Users.showSignOutDialog(this));

        initializeListFragment();
        initializeDetailFragment();
        UserPrivileges.fetchFor(UserPrivileges.DISABLED_ADS, hasPrivilege -> {
            if (!hasPrivilege) {
                setupBannerAd();
            }
        }, mUser.getUid());

//        CountdownAppInvites.handleAppInvite(getIntent()).addOnSuccessListener(this, data -> {
//            if (data == null) {
//                Log.d(TAG, "getInvitation: no data");
//                return;
//            }
//
//            // Get the deep link
//            Uri deepLink = data.getLink();
//
//            // Extract invite
//            FirebaseAppInvite invite = FirebaseAppInvite.getInvitation(data);
//            if (invite != null) {
//                String invitationId = invite.getInvitationId();
//            }
//        }).addOnFailureListener(this, e -> {
//            Log.w(TAG, "handleInvite: Error when fetching app invite data", e);
//        });
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBannerAd != null) {
            mBannerAd.pause();
        }
    }

    @Override
    public void onProvideAssistContent(AssistContent outContent) {
        super.onProvideAssistContent(outContent);
        // TODO: 7/3/17 Implement me
//            outContent.setStructuredData(AssistantIndex.getStructuredContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBannerAd != null) {
            mBannerAd.resume();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_countdown_list, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBannerAd != null) {
            mBannerAd.destroy();
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.main_action_mode, menu);
        mode.setTitle(R.string.label_countdowns_select);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sort) {//                SortOptionDialog sortDialog = SortOptionDialog.newInstance(
//                        ((CountdownRecyclerAdapter) mCountdownList.getAdapter()).getSortOption());
//                sortDialog.show(getSupportFragmentManager(), "SortOptionDialog");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_share) {// TODO: 3/21/17 Implement Firebase App Invites in another app function
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mActionMode = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.v(TAG, "onNavigationItemSelected: " + item);
//        switch (item.getItemId()) {
//            case R.id.action_main:
//                break;
//            case R.id.action_settings:
//                SettingsActivity.start(this);
//                break;
//            case R.id.action_help:
//                HelpConfig config = new HelpConfig.Builder()
//                        .setEmailAddress("admin@thecraft.company").build();
//                HelpActivity.start(this, config);
//                break;
//            case R.id.action_feedback:
//                FeedbackActivity.start(this);
//                break;
//        }
//        mDrawerManager.closeDrawer();
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab_create_countdown) {
            CountdownCreationActivity.start(this);
        }
    }

    @Override
    public void onCountdownSelected(String countdownId) {
        Log.d(TAG, "onCountdownSelected: Selected countdown ID is " + countdownId);
        CountdownAnalytics.getInstance(this).logSelection(countdownId);
        showCountdownDetails(countdownId);
    }

    @Override
    public void onCountdownLongSelected(String countdownId) {
        Log.v(TAG, "onCountdownLongSelected: " + countdownId);
        // TODO: 3/19/17 select item in adapter
//        if (!mSelectedItemIds.contains(countdownId)) {
//            mSelectedItemIds.add(countdownId);
//        } else {
//            mSelectedItemIds.remove(countdownId);
//        }
//        if (mActionMode != null) {
//            return;
//        } else {
//            mActionMode = startSupportActionMode(this);
//        }
//        if (mSelectedItemIds.size() == 0) {
//            mActionMode.finish();
//        }
    }

    @Override
    public void onSortSelection(@SortOption int option) {
        sortList(option);
    }

    @Override
    public void onLoad() {
        // Let fragment handle this
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        Log.d(TAG, "handleIntent: Intent action is " + action);
        Log.d(TAG, "handleIntent: Intent data is " + data);
        if (action == null) {
            return;
        }
        switch (action) {
            case Intent.ACTION_VIEW:
                if (data != null) {
                    String countdownId = data.substring(data.lastIndexOf("/") + 1);
                    showCountdownDetails(countdownId);
                }
                break;
            case ACTION_VIEW_COUNTDOWN_DETAILS:
                String countdownId = intent.getStringExtra(IntentUtils.ARG_COUNTDOWN_ID);
                showCountdownDetails(countdownId);
                break;
            case ACTION_FEATURE_DISCOVERY:
                startFeatureDiscovery();
                break;
        }
    }

    private void initializeListFragment() {
        Fragment fragment = CountdownListFragment.newInstance(mUser.getUid());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_countdown_list, fragment, "CountdownListFragment")
                .commit();
    }

    private void initializeDetailFragment() {
        if (findViewById(R.id.coordinator_root_details) != null) {
            // TODO: 7/6/17 Show tablet placeholder view
            Log.d(TAG, "initializeDetailFragment: Setting up wide-screen detail view");
            return;
        }
        Fragment fragment = CountdownPersistentDetailFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_countdown_details, fragment,
                        "CountdownDetailFragment")
                .commit();
    }

    private void showCountdownDetails(String countdownId) {
        // TODO: 6/24/17 Find more OOP way to do this
        CountdownDetailDisplay display = (CountdownDetailDisplay) getSupportFragmentManager()
                .findFragmentByTag("CountdownDetailFragment");
        display.setCountdownId(countdownId);
        display.showDisplay(getSupportFragmentManager());
    }

    private void startFeatureDiscovery() {
        TapTargetView.showFor(this, TapTarget.forView(findViewById(R.id.fab_create_countdown),
                getString(R.string.label_create_countdown),
                getString(R.string.label_create_countdown_detail))
                        .icon(ResourcesCompat.getDrawable(getResources(),
                                R.drawable.ic_alarm_add_white_24dp, getTheme())),
                new Listener() {
                    @Override
                    public void onTargetClick(TapTargetView view) {
                        super.onTargetClick(view);
                        Log.d(TAG, "onTargetClick: Countdown creation feature discovery "
                                + "engagement");
                        CountdownCreationActivity.start(CountdownListActivity.this);
                    }
                });
    }

    @SuppressWarnings("unused")
    private void sortList(@SortOption int option) {
        // TODO: 7/2/17 Implement list sorting
        Query keyQuery = OldDatabase.getUserCountdownsReference(mUser.getUid());
//        CountdownRecyclerAdapter.sortList(mCountdownList, this, keyQuery, option);
    }

    @SuppressWarnings("unused")
    private void launchInvite(Countdown countdown) {
        // TODO: 6/25/17 Implement proper invite system
//        Intent shareIntent = new AppInviteInvitation.IntentBuilder("Hello")
//                .setMessage("Hey, do you remember " + countdown.getTitle() + "?")
//                .setCallToActionText("Countdown now.")
//                .build();
//        startActivityForResult(shareIntent, RC_APP_INVITE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: 7/1/17 Detect detail sheet state and collapse if necessary
    }

    private void setupBannerAd() {
        List<String> testDeviceIds = Arrays.asList(AdRequest.DEVICE_ID_EMULATOR, getString(R.string.test_device_id));
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        MobileAds.initialize(getApplicationContext());
        AdRequest request = new Builder()
                .build();
        mBannerAd = findViewById(R.id.ad_banner_home);
        mBannerAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mBannerAd.setVisibility(View.VISIBLE);
            }
        });
        mBannerAd.loadAd(request);
    }
}
