package com.craft.apps.countdowns;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatDialogFragment;
import com.craft.apps.countdowns.common.model.SortOptions;
import com.craft.apps.countdowns.common.model.SortOptions.SortOption;
import java.util.Objects;

/**
 * A dialog that allows the selection of a {@link SortOptions}.
 *
 * @author willie
 * @version 1.0.0
 * @since 5/26/17
 */
// TODO: 5/27/17 Persist this to SharedPreferences
public class SortOptionDialog extends AppCompatDialogFragment implements
        DialogInterface.OnClickListener {

    /**
     * The initially selected sort startExtraSettingsIntent for the dialog, required for
     * instantiation.
     *
     * @see SortOptions
     */
    public static final String ARG_INITIAL_SELECTION = "initial_selection";
    private static final String TAG = SortOptionDialog.class.getSimpleName();
    @SortOption
    private int mSortOption;

    private SelectionListener mListener;

    /**
     * A factory startExtraSettingsIntent that creates and initializes a dialog with a selection
     *
     * @param sortOption The dialog's initial sorting startExtraSettingsIntent
     * @return A new SortOptionDialog
     */
    @NonNull
    public static SortOptionDialog newInstance(@SortOption int sortOption) {
        Bundle args = new Bundle();
        args.putInt(ARG_INITIAL_SELECTION, sortOption);
        SortOptionDialog fragment = new SortOptionDialog();
        fragment.setArguments(args);
        return fragment;
    }

    // TODO: 5/27/17 Possibly use defaults from SharedPreferences
//    @NonNull
//    public static SortOptionDialog newInstance() {
//        return new SortOptionDialog();
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectionListener) {
            mListener = (SelectionListener) context;
        } else {
            throw new RuntimeException(context + " must implement SelectionListener.");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ARG_INITIAL_SELECTION)) {
            mSortOption = getArguments().getInt(ARG_INITIAL_SELECTION);

        } else {
            throw new IllegalArgumentException("Dialog's arguments must contain an "
                    + "initial_selection key!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Builder(getContext())
                .setTitle(R.string.title_dialog_sort_countdowns)
                .setSingleChoiceItems(R.array.label_sort_by, mSortOption, this)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this)
                .create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                mListener.onSortSelection(mSortOption);
                dismiss();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                dialog.cancel();
                break;
        }
        // We don't have to worry about neutral button.
        // TODO: 5/27/17 Find less troublesome way to do this
        String[] options = getContext().getResources()
                .getStringArray(R.array.label_sort_by);
        if (Objects.equals(options[0], options[which])) {
            mSortOption = SortOptions.DATE_CREATED;
        } else if (Objects.equals(options[1], options[which])) {
            mSortOption = SortOptions.TIME_LEFT;
        } else if (Objects.equals(options[2], options[which])) {
            mSortOption = SortOptions.COUNTDOWN_LENGTH;
        }
    }

    /**
     * A callback that's called when a selection is chosen.
     *
     * @see SortOptionDialog
     */
    public interface SelectionListener {

        /**
         * Notifies the listener that an item was selected.
         *
         * @param option A way creator sorting a list creator {@link com.craft.apps.countdowns.common.model.Countdown}s
         */
        void onSortSelection(@SortOption int option);
    }
}
