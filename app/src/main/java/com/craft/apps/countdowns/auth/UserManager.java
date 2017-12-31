package com.craft.apps.countdowns.auth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.UserRepository;
import com.craft.apps.countdowns.common.model.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A utility class for managing user state.
 *
 * @version 1.0.0
 * @since 2.0.0
 */
public final class UserManager {

    private static final String TAG = UserManager.class.getSimpleName();

    private UserManager() {
        // no-op
    }

    /**
     * Gets the currently signed in user if one exists.
     *
     * @return The currently signed in {@link User}; null if there is none signed in.
     */
    @Nullable
    public static User getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return UserRepository.fetchUser(user.getUid());
        }
        return null;
    }

    /**
     * Signs out the currently signed-in user if one exists.
     *
     * @param activity An activity to scope this operation.
     * @return A task representing the state of the operation
     */
    public static Task<Void> signOut(FragmentActivity activity) {
        return AuthUI.getInstance().signOut(activity);
    }

    /**
     * This links an old account's credentials to a new login.
     * <p>
     * This also copies the old user's data into the new user location.
     *
     * @param activity An activity to scope this operation
     * @param credential A credential obtained from sign-out
     */
    public static void linkAccount(final Activity activity, AuthCredential credential) {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.linkWithCredential(credential).addOnCompleteListener(
                    activity, new LinkAccountCompletionListener(activity, user));
        }
    }

    /**
     * A listener that links one account credentials to a new account.
     */
    public static class LinkAccountCompletionListener implements OnCompleteListener<AuthResult> {

        private final FirebaseUser mUser;
        private final Activity mActivity;

        /**
         * Creates a new listener that links account credentials when called.
         *
         * @param user The user to copy old credentials from
         */
        public LinkAccountCompletionListener(Activity activity, FirebaseUser user) {
            mActivity = activity;
            mUser = user;
        }

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                Toast.makeText(mActivity, R.string.response_account_linked, Toast.LENGTH_SHORT)
                        .show();
                // Move the old data to the new reference
                final String newUserId = task.getResult().getUser().getUid();
                User oldUser = UserRepository.fetchUser(mUser.getUid());
                UserRepository.updateUser(newUserId, oldUser)
                        .addOnFailureListener(e -> {
                            Log.w(TAG, "Couldn't update user data", e);
                        });
            }
        }
    }
}
