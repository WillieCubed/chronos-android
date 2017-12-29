package com.craft.apps.countdowns;

import android.app.assist.AssistContent;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
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
import com.craft.apps.countdowns.common.viewmodel.SelectedCountdownViewModel;
import com.craft.apps.countdowns.invites.CountdownAppInvites;
import com.craft.apps.countdowns.util.Users;
import com.craft.essentials.ui.DrawerManager;
import com.craft.essentials.userhelp.activity.FeedbackActivity;
import com.craft.essentials.userhelp.activity.HelpActivity;
import com.craft.essentials.userhelp.model.HelpConfig;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;
import com.getkeepsafe.taptargetview.TapTargetView.Listener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.appinvite.FirebaseAppInvite;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_FEATURE_DISCOVERY;
import static com.craft.apps.countdowns.common.util.IntentUtils.ACTION_VIEW_COUNTDOWN_DETAILS;

/**
 * An {@link android.app.Activity} that lists {@link Countdown}s using a {@link
 * CountdownListFragment}
 * <p> Currently, this is the entry point to the app and the primary screen
 * for interaction. Users can view countdown information, create new countdowns, and navigate to the
 * {@link SettingsActivity} from here.
 *
 * @version 1.0.1
 * @see CountdownListFragment
 * @see CountdownCreationActivity
 * @see SettingsActivity
 * @since 1.0.0
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

        CountdownAppInvites.handleAppInvite(getIntent()).addOnSuccessListener(this, data -> {
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
        }).addOnFailureListener(this, e -> {
            Log.w(TAG, "handleInvite: Error when fetching app invite data", e);
        });
        handleIntent(getIntent());

        SelectedCountdownViewModel viewModel = ViewModelProviders.of(this)
                .get(SelectedCountdownViewModel.class);
        viewModel.getSelectedCountdown()
                .observe(this, this::showCountdownDetails);
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
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            // TODO: 7/3/17 Implement me
//            outContent.setStructuredData(AssistantIndex.getStructuredContent());
        }
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
        switch (item.getItemId()) {
            case R.id.action_sort:
//                SortOptionDialog sortDialog = SortOptionDialog.newInstance(
//                        ((CountdownRecyclerAdapter) mCountdownList.getAdapter()).getSortOption());
//                sortDialog.show(getSupportFragmentManager(), "SortOptionDialog");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                // TODO: 3/21/17 Implement Firebase App Invites in another app function
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
        switch (item.getItemId()) {
            case R.id.action_main:
                break;
            case R.id.action_settings:
                SettingsActivity.start(this);
                break;
            case R.id.action_help:
                HelpConfig config = new HelpConfig.Builder()
                        .setEmailAddress("admin@thecraft.company").build();
                HelpActivity.start(this, config);
                break;
            case R.id.action_feedback:
                FeedbackActivity.start(this);
                break;
        }
        mDrawerManager.closeDrawer();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_create_countdown:
                CountdownCreationActivity.start(this);
                break;
        }
    }

    @Override
    public void onCountdownSelected(String countdownId) {
        Log.d(TAG, "onCountdownSelected: Selected countdown ID is " + countdownId);
        selectCountdown(countdownId);
    }

    @Override
    public void onCountdownLongSelected(String countdownId) {
        Log.v(TAG, "onCountdownLongSelected: " + countdownId);
        // TODO: 3/19/17 select item in adapter
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
                    selectCountdown(countdownId);
                }
                break;
            case ACTION_VIEW_COUNTDOWN_DETAILS:
                String countdownId = intent.getStringExtra(IntentUtils.ARG_COUNTDOWN_ID);
                selectCountdown(countdownId);
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
        // TODO: 12/28/2017 Fix tablet functionality
    }

    private void showCountdownDetails(String countdownId) {
        CountdownAnalytics.getInstance(this).logSelection(countdownId);
        CountdownDetailFragment fragment = CountdownDetailFragment.newInstance(countdownId);
        fragment.show();
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
        Intent shareIntent = new AppInviteInvitation.IntentBuilder("Hello")
                .setMessage("Hey, do you remember " + countdown.getTitle() + "?")
                .setCallToActionText("Countdown now.")
                .build();
        startActivityForResult(shareIntent, RC_APP_INVITE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // TODO: 7/1/17 Detect detail sheet state and collapse if necessary
    }

    private void setupBannerAd() {
        MobileAds.initialize(getApplicationContext(),
                getString(R.string.ad_unit_home_screen_banner));
        AdRequest request = new Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device_id))
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

    private void selectCountdown(String countdownId) {
        SelectedCountdownViewModel viewModel = ViewModelProviders.of(CountdownListActivity.this)
                .get(SelectedCountdownViewModel.class);
        viewModel.setSelectedCountdown(countdownId);
    }
}
