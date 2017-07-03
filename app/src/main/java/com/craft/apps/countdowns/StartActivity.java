package com.craft.apps.countdowns;

import static com.craft.apps.countdowns.util.Users.RC_SIGN_IN;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.craft.apps.countdowns.common.settings.Preferences;
import com.craft.apps.countdowns.notification.NotificationSender;
import com.craft.apps.countdowns.util.Users;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

public class StartActivity extends AppCompatActivity {

    private static final String TAG = StartActivity.class.getSimpleName();

    public static void start(Context context) {
        Intent starter = new Intent(context, StartActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            NotificationSender.initializeChannels(this);
        }
        if (Users.getCurentUser() != null) {
            if (!Preferences.getInstance(this).isOnboarded()) {
//                OnboardingActivity.startObserving(this);
                CountdownListActivity.start(this);
            } else {
                CountdownListActivity.start(this);
            }
        } else {
            Log.d(TAG, "onCreate: Launching sign in");
            Users.launchSignIn(this);
        }
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SIGN_IN:
                if (data != null) {
                    // TODO: 3/18/17 Refactor into Users.java
                    IdpResponse response = IdpResponse.fromResultIntent(data);

                    if (resultCode == RESULT_OK) {
                        recreate();
                    } else {
                        if (response == null) {
                            return;
                        }

                        // TODO: 5/6/17 Better handling of network outages
                        if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                            Toast.makeText(this, "Please connect to a network to sign in",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                            Toast.makeText(this, "An unknown error occurred. Please try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }
}
