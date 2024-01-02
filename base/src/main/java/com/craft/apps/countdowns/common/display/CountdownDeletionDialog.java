package com.craft.apps.countdowns.common.display;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.craft.apps.countdowns.R;
import com.craft.apps.countdowns.common.database.OldDatabase;
import com.craft.apps.countdowns.common.util.CountdownPreconditions;

import java.util.Objects;

/**
 * @author willie
 * @version 1.0.0
 * @since v1.0.0 (6/30/17)
 */

public class CountdownDeletionDialog extends DialogFragment {

    private static final String TAG = CountdownDeletionDialog.class.getSimpleName();

    private String mCountdownId;

    private String mUserId;

    private CountdownDeletionListener mCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment parent = getParentFragment();
        if (isCallbackInstance(parent)) {
            mCallback = (CountdownDeletionListener) parent;
        } else {
            throw new RuntimeException("Context must be instance of CountdownDeletionListener");
        }

        mCountdownId = CountdownPreconditions.checkValidArgs(getArguments());
        if (requireArguments().getString("user_id") != null) {
            mUserId = requireArguments().getString("user_id");
        } else {
            throw new IllegalArgumentException("Arguments must include user_id argument!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Builder(requireContext())
                .setTitle(R.string.query_dialog_delete_countdown)
                .setMessage(R.string.query_dialog_delete_countdown_details)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    // TODO: 6/30/17 Fix this shit
//                    CountdownManager.deleteCountdown(getContext(), user.getUid(), countdownId);
                    OldDatabase.deleteUserCountdown(mCountdownId, mUserId,
                            (databaseError, databaseReference) -> {
                                if (databaseError != null) {
                                    mCallback.onDeletion(databaseReference.getKey());
                                }
                            });
                    dialog.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.cancel())
                .create();
    }

    private boolean isCallbackInstance(Fragment fragment) {
        return fragment instanceof CountdownDeletionListener;
    }

    public interface CountdownDeletionListener {

        void onDeletion(String countdownId);
    }
}
