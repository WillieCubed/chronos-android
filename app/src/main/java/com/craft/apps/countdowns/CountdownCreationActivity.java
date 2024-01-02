package com.craft.apps.countdowns;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import com.craft.apps.countdowns.common.CountdownCreator;
import com.craft.apps.countdowns.common.database.CountdownManager;
import com.craft.apps.countdowns.common.format.SimpleDateFormatter;
import com.craft.apps.countdowns.common.format.SimpleTimeFormatter;
import com.craft.apps.countdowns.common.model.Countdown;
import com.craft.apps.countdowns.common.util.DateUtility;
import com.craft.apps.countdowns.util.Users;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;

// TODO: 7/1/17 Delegate to a fragment

/**
 * A screen that allows users to create a {@link Countdown}
 * <p>
 * This lets a user choose a title, description, time, date, timezone, and color for countdown
 * creation.
 *
 * @author willie
 * @version 1.0.0
 * @see Countdown
 * @since v1.0.0 (3/18/17)
 */
public class CountdownCreationActivity extends AppCompatActivity implements
        OnClickListener {

    private static final String TAG = CountdownCreationActivity.class.getSimpleName();

    private FirebaseUser mUser;

    private CountdownCreator mCountdownCreator;

    private TextInputLayout mTitleLayout;
    private TextInputLayout mDescriptionLayout;
    private TextView mDateView;
    private TextView mTimeView;

    private DatePickerDialog mDateDialog;

    private TimePickerDialog mTimeDialog;

    public static void start(Context context) {
        Intent starter = new Intent(context, CountdownCreationActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_creation);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mUser = Users.getCurentUser();
        if (mUser == null) {
            // TODO: 6/30/17 Find more thorough solution
            StartActivity.start(this);
            finish();
            return;
        }

        mCountdownCreator = CountdownCreator.newBuilder();
        DateUtility todayUtil = new DateUtility();

        mTitleLayout = findViewById(R.id.input_countdown_name);
        mDescriptionLayout = findViewById(R.id.input_countdown_description);
        //noinspection ConstantConditions The layout includes an EditText
        mTitleLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                mCountdownCreator.setTitle(String.valueOf(text));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no-op
            }
        });
        //noinspection ConstantConditions The layout includes an EditText
        mDescriptionLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
                // no-op
            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                mCountdownCreator.setDescription(String.valueOf(text));
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // no-op
            }
        });

        mDateView = findViewById(R.id.input_countdown_date);
        mTimeView = findViewById(R.id.input_countdown_time);

        mDateDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            mCountdownCreator.setYear(year);
            mCountdownCreator.setMonth(month + 1); // Account for framework dialog starting at 0
            mCountdownCreator.setDayOfMonth(dayOfMonth);
            // Time doesn't matter
            DateUtility dateUtil = new DateUtility(year, month + 1, dayOfMonth, 0, 0);
            mDateView.setText(SimpleDateFormatter.getLocalDate(this, dateUtil.getEndMillis()));
        }, todayUtil.getYear(), todayUtil.getMonthOfYear() - 1, todayUtil.getDayOfMonth());
        mDateDialog.getDatePicker().setMinDate(todayUtil.getEndMillis());
        mTimeDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            mCountdownCreator.setHour(hourOfDay);
            mCountdownCreator.setMinute(minute);
            // Date doesn't matter
            DateUtility timeUtil = new DateUtility(1970, 1, 1, hourOfDay, minute);
            mTimeView.setText(SimpleTimeFormatter.getLocalTime(this, timeUtil.getEndMillis()));

        }, 0, 0, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.countdown_creation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_finish) {
            if (hasValidInputs()) {
                mCountdownCreator.updateStart();
                uploadCountdown(mCountdownCreator.build());
            } else {
                validateInputs();
            }
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.input_countdown_date) {
            Log.v(TAG, "onClick: Date selected");
            mDateDialog.show();
        } else if (id == R.id.input_countdown_time) {
            Log.v(TAG, "onClick: Time selected");
            mTimeDialog.show();
        }
    }

    private void validateInputs() {
        mTitleLayout.setError("Please choose a title");
        mTitleLayout.setErrorEnabled(true);
    }

    private boolean hasValidInputs() {
        // STOPSHIP: 6/30/17 Validate inputs
        // Check if the defaults are selected
//        return !((mCountdownDateTime.minuteOfDay().get() == 0
//                && mCountdownDateTime.hourOfDay().get() == 0)
//                || mTitleLayout.getEditText().toString().isEmpty());
        return true;
    }

    private void uploadCountdown(Countdown countdown) {
        // TODO: 7/2/17 use updateChildren in Countdown Manager
        String countdownId = CountdownManager.getNewCountdownId();
        Tasks.whenAll(CountdownManager.uploadCountdown(countdown, countdownId),
                CountdownManager.addCountdownToUser(countdownId, mUser.getUid()))
                .addOnSuccessListener(this, void1 -> {
                    Log.d(TAG, "uploadCountdown: Countdown successfully uploaded");
//                    Indexer.indexCountdown(countdown, countdownId, mUser);
                    finish();
                })
                .addOnFailureListener(this, exception -> {
                    Log.w(TAG, "uploadCountdown: Error when uploading countdown", exception);
                    Toast.makeText(CountdownCreationActivity.this, "Error when uploading countdown",
                            Toast.LENGTH_SHORT).show();
                    finish();
                });

    }
}
