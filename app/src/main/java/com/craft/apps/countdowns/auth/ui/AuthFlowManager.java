package com.craft.apps.countdowns.auth.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.craft.apps.countdowns.BuildConfig;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.StartActivity;
import com.craft.apps.countdowns.auth.UserManager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.appindexing.FirebaseAppIndex;

import java.util.Arrays;
import java.util.List;

/**
 * A utility class that shows dialogs for sign-in, sign-out, and other user management
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public final class AuthFlowManager {

    /**
     * A request code that indicates an activity should start for sign in.
     */
    public static final int RC_SIGN_IN = 1;

    private static final String TAG = AuthFlowManager.class.getSimpleName();

    private AuthFlowManager() {
        // no-op
    }

    /**
     * Starts a sign-in flow.
     *
     * @param activity The activity to return sign in flow results
     */
    public static void launchSignIn(Activity activity) {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setLogo(R.drawable.app_icon_large)
                .setAvailableProviders(providers)
                .build();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Opens a dialog that allows the user to switch the currently signed-in account.
     *
     * @param activity An activity to call {@link UserManager#signOut(FragmentActivity)}
     */
    public static void showSwitchAccountDialog(final FragmentActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.query_auth_switch_account_short)
                .setMessage(R.string.query_auth_switch_accounts_long)
                .setNegativeButton(android.R.string.no, (dialog1, which) -> dialog1.cancel())
                .setPositiveButton(android.R.string.yes, (dialog2, which) -> {
                    dialog2.dismiss();
                    UserManager.signOut(activity).addOnCompleteListener(task -> {
                        Log.i(TAG, "Sign out complete");
                        StartActivity.start(activity);
                    });
                })
                .create();
        dialog.show();
    }

    /**
     * Opens a dialog that allows the user to completely sign out of the app.
     *
     * @param activity An activity to call {@link UserManager#signOut(FragmentActivity)}
     */
    public static void showSignOutDialog(final FragmentActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.query_sign_out_short)
                .setMessage(R.string.query_sign_out_long)
                .setNegativeButton(android.R.string.no, (dialog1, which) -> dialog1.cancel())
                .setPositiveButton(android.R.string.yes, (dialog2, which) -> {
                    dialog2.dismiss();
                    UserManager.signOut(activity)
                            .addOnFailureListener(e -> Log.w(TAG, "Error when signing out", e))
                            .addOnSuccessListener(activity, aVoid -> {
                                Log.i(TAG, "Sign out successful");
                                FirebaseAppIndex.getInstance().removeAll();
                                StartActivity.start(activity);
                                activity.finish();
                            });
                })
                .create();
        dialog.show();
    }
}
