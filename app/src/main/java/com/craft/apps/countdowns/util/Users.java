package com.craft.apps.countdowns.util;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.FragmentActivity;

import com.craft.apps.countdowns.BuildConfig;
import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthUI.IdpConfig;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// TODO: 7/1/17 Big pile of cleanup

/**
 * @author willie
 * @version 0.1.0
 * @since v1.0.0 (3/18/17)
 */
public class Users {

    public static final int RC_SIGN_IN = 1;

    private static final String TAG = Users.class.getSimpleName();

    /**
     * Launches a FirebaseUI sign in flow
     * <p>
     * Must be handled like so:
     * {@code
     * protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     * if (requestCode == Users.RC_SIGN_IN) {
     * if (resultCode == RESULT_OK) {
     * <p>
     * }
     * }
     * }
     * }
     */
    public static void launchSignIn(Activity activity) {
        List<IdpConfig> providers = Arrays.asList(
                new IdpConfig.EmailBuilder().build(),
                new IdpConfig.GoogleBuilder().build());
        Intent signInIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setIsSmartLockEnabled(!BuildConfig.DEBUG)
                .setLogo(R.drawable.app_icon_large)
                .setAvailableProviders(providers)
                .build();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public static Task<Void> signOut(FragmentActivity activity) {
        return AuthUI.getInstance().signOut(activity);
    }

    public static void linkAccount(final Activity activity, AuthCredential credential) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.linkWithCredential(credential).addOnCompleteListener(
                    activity, new LinkAccountCompletionListener(activity, user));
        }
    }

    public static void showSwitchAccountDialog(final FragmentActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setTitle("Switch account?")
                .setMessage("Would you like to switch accounts?")
                .setNegativeButton(android.R.string.no, (dialog1, which) -> dialog1.cancel())
                .setPositiveButton(android.R.string.yes, (dialog2, which) -> {
                    dialog2.dismiss();
                    signOut(activity).addOnCompleteListener(task -> {
                        Log.i("Users", "Sign out complete");
//                        StartActivity.start(activity);
                    });
                })
                .create();
        dialog.show();
    }

    /**
     * Shows an {@link AlertDialog} allowing a user to
     */
    public static void showSignOutDialog(final FragmentActivity activity) {
        AlertDialog dialog = new Builder(activity)
                .setTitle(R.string.query_sign_out_short)
                .setMessage(R.string.query_sign_out_long)
                .setNegativeButton(android.R.string.no, (dialog1, which) -> dialog1.cancel())
                .setPositiveButton(android.R.string.yes, (dialog2, which) -> {
                    dialog2.dismiss();
                    signOut(activity)
                            .addOnFailureListener(e -> Log.w(TAG, "Error when signing out", e))
                            .addOnSuccessListener(activity, aVoid -> {
                                Log.i(TAG, "Sign out successful");
//                                FirebaseAppIndex.getInstance().removeAll();
//                                StartActivity.start(activity);
                                activity.finish();
                            });
                })
                .create();
        dialog.show();
    }

    /**
     * @return The currently signed in {@link FirebaseUser}
     */
    public static FirebaseUser getCurentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public static String fetchTokenFromResponse(IdpResponse response) {
        return response.getIdpToken();
    }

    public static void handleSignIn(Intent data, SignInCallback callback) {

    }

    public interface SignInCallback {

        void onSignIn();
    }

    public static class LinkAccountCompletionListener implements OnCompleteListener<AuthResult> {

        private final FirebaseUser mUser;
        private final Activity mActivity;

        public LinkAccountCompletionListener(Activity activity, FirebaseUser user) {
            mActivity = activity;
            mUser = user;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(mActivity,
                        R.string.response_account_linked,
                        Toast.LENGTH_SHORT).show();
                // Move the old data to the new reference
                final String newUserId = task.getResult().getUser().getUid();
                OldDatabase.getUserCountdownsReference(mUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Map<String, Object> childUpdates = new HashMap<>();

                                for (DataSnapshot snapshot :
                                        dataSnapshot.getChildren()) {
                                    childUpdates.put(snapshot.getKey(), true);
                                }

                                FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(newUserId)
                                        .child("countdowns")
                                        .updateChildren(childUpdates);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
            }
        }
    }
}
