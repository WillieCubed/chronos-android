package com.craft.apps.countdowns;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.craft.apps.countdowns.common.settings.Preferences;
import com.github.paolorotolo.appintro.AppIntro;

public class OnboardingActivity extends AppIntro {

    public static void start(Context context) {
        Intent starter = new Intent(context, OnboardingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        addSlide(AppIntroFragment.newInstance(getString(R.string.label_onboarding_title_1),
//                getString(R.string.label_onboarding_description_1),
//                R.drawable.app_icon_large,
//                getResources().getColor(R.color.colorPrimary)));
        // TODO: 5/14/17 Find use for this activity
        finishOnboarding();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finishOnboarding();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finishOnboarding();
    }

    private void finishOnboarding() {
        Preferences.getInstance(this).setOnboardingState(this, true);
        CountdownListActivity.fromOnboarding(this);
        finish();
    }
}
