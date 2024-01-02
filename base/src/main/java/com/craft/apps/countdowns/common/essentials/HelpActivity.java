package com.craft.apps.countdowns.common.essentials;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.craft.apps.countdowns.R;


/**
 * An {@link android.app.Activity} that allows a user to email, call, visit a website, or send
 * feedback.
 *
 * @version 1.0.0
 * @see FeedbackActivity
 * @since 1.0.0
 */
public class HelpActivity extends AppCompatActivity implements OnClickListener {

    /**
     * A constant marking an extra as deserializable to a {@link HelpConfig}
     *
     * @see HelpConfig
     */
    public static final String EXTRA_HELP_CONFIG = "com.craft.essentials.extra.config_help";

    private static final String TAG = HelpActivity.class.getSimpleName();

    private HelpConfig mHelpConfig;

    private View mCallButton;

    private View mEmailButton;

    /**
     * Starts a new instance of this activity.
     *
     * @param config A HelpConfig object to initialize UI
     */
    public static void start(Context context, HelpConfig config) {
        Intent starter = new Intent(context, HelpActivity.class);
        starter.putExtra(EXTRA_HELP_CONFIG, config);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        if (getIntent().hasExtra(EXTRA_HELP_CONFIG)) {
            mHelpConfig = getIntent().getParcelableExtra(EXTRA_HELP_CONFIG);
        } else {
            Toast.makeText(HelpActivity.this, "Couldn't fetch help information", Toast.LENGTH_SHORT)
                    .show();
            Log.e(TAG, "onCreate: HelpConfig extra shouldn't be null.");
        }
        mCallButton = findViewById(R.id.button_help_call);
        if (mHelpConfig.getPhoneNumber() != null) {
            mCallButton.setVisibility(View.VISIBLE);
        }
        mEmailButton = findViewById(R.id.button_help_email);
        if (mHelpConfig.getEmailAddress() != null) {
            mEmailButton.setVisibility(View.VISIBLE);
        }
        View feedbackButton = findViewById(R.id.button_help_send_feedback);
        // TODO: 6/30/17 Re-enable when feedback is supported
//        feedbackButton.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.acitivity_help, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_privacy)
                .setVisible(mHelpConfig.getPrivacyPolicyUrl() != null);
        menu.findItem(R.id.action_tos)
                .setVisible(mHelpConfig.getTosUrl() != null);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_privacy) {
            openWebPage(mHelpConfig.getPrivacyPolicyUrl());
            return true;
        } else if (itemId == R.id.action_tos) {
            openWebPage(mHelpConfig.getTosUrl());
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_help_call) {
            callHelp(mHelpConfig.getPhoneNumber());
        } else if (viewId == R.id.button_help_email) {
            sendEmail(mHelpConfig.getEmailAddress());
        } else if (viewId == R.id.button_help_send_feedback) {
            sendFeedback();
        }
    }

    /**
     * Launches the given URL in the device's default browser.
     */
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Starts the dedicated user feedback activity.
     *
     * @see FeedbackActivity
     */
    public void sendFeedback() {
        // TODO: 5/7/17 Add customizable activity for feedback
        FeedbackActivity.start(this);
    }

    private void sendEmail(@NonNull String emailAddress) {
        sendEmail(emailAddress, null);
    }

    private void sendEmail(@NonNull String address, @Nullable String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(Uri.parse("mailto:"))
                .putExtra(Intent.EXTRA_EMAIL, address);
        if (subject != null) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void callHelp(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);

        }
    }
}
