package com.craft.apps.countdowns;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author willie
 * @version 1.0.0
 * @since v2.0.0 (6/28/17)
 */
public class CountdownDisplayActivity extends Activity {

    private static final String TAG = CountdownDisplayActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_display);
    }
}
