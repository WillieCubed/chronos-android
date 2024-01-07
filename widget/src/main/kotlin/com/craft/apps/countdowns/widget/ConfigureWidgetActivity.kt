package com.craft.apps.countdowns.widget

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.glance.appwidget.GlanceAppWidgetManager
import com.craft.apps.countdowns.theme.LogDateTheme
import com.craft.apps.countdowns.widget.ui.ConfigureWidgetScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConfigureWidgetActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val manager = GlanceAppWidgetManager(this)
        val id = manager.getGlanceIdBy(intent)

        setContent {
            LogDateTheme {
                ConfigureWidgetScreen()
            }
        }
    }
}
