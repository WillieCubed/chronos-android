package com.craft.apps.countdowns.feature.home

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.craft.apps.countdowns.testing.HiltActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class HomeFeatureTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<HiltActivity>()

    @Test
    fun `test item is displayed`() {
        composeTestRule.setContent {
            HomeRoute(onCountdownSelected = {}, onPinCountdown = {})
        }
        composeTestRule.onNodeWithTag("item_1").assertExists()
    }
}