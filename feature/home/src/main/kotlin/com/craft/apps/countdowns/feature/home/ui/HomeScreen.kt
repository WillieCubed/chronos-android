package com.craft.apps.countdowns.feature.home.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.model.Countdown
import com.craft.apps.countdowns.feature.home.R
import com.craft.apps.countdowns.ui.CountdownCreationSheet
import com.craft.apps.countdowns.ui.HomeEmptyView
import com.craft.apps.countdowns.ui.theme.ChronosTheme
import com.craft.apps.countdowns.ui.util.testCountdowns
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onAddCountdown: (countdown: Countdown) -> Unit,
    onDeleteCountdown: (countdownId: Int) -> Unit,
    onCountdownSelected: (countdownId: Int) -> Unit,
    onPinCountdown: (countdownId: Int) -> Unit,
    onFeatureCountdown: (countdownId: Int) -> Unit,
    modifier: Modifier = Modifier,
    shouldCreateNewCountdown: Boolean = false,
) {
    val initialSheetState = if (shouldCreateNewCountdown) {
        ModalBottomSheetValue.Expanded
    } else {
        ModalBottomSheetValue.Hidden
    }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(initialSheetState)
    var showBottomSheet by remember { mutableStateOf(shouldCreateNewCountdown) }
    var shouldChangeAppBarColor by remember {
        mutableStateOf(false)
    }

    fun openSheet() {
        Log.d("HomeScreen", "Opening sheet")
        scope.launch { sheetState.show() }.invokeOnCompletion {
            showBottomSheet = true
        }
    }

    fun hideSheet() {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }

    fun handleCountdownCreation(label: String, expiration: Instant, notes: String) {
        val countdown = Countdown(0, label, expiration, Clock.System.now(), notes, false)
        onAddCountdown(countdown)
        hideSheet()
    }

    val appBarContainerColor = if (shouldChangeAppBarColor) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }

    // TODO: Maybe just screw the bottom sheet for now
    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            CountdownCreationSheet(onTriggerCreation = ::handleCountdownCreation)
        },
    ) {
        Scaffold(
            modifier = modifier,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.primary,
                    text = { Text(stringResource(R.string.action_label_new_countdown)) },
                    icon = { Icon(Icons.Filled.Add, contentDescription = null) },
                    onClick = {
                        openSheet()
                    },
                )
            },
            topBar = {
                MediumTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(appBarContainerColor),
                    title = { Text(stringResource(id = R.string.screen_home_title)) },
                    actions = {
                        IconButton(onClick = {
                            /* TODO: Open options menu with settings */
                        }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = stringResource(R.string.content_description_more_options),
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .fillMaxSize(),
            ) {
                when (state) {
                    is HomeUiState.Success -> {
                        val countdowns = state.countdowns
                        if (countdowns.isEmpty()) {
                            HomeEmptyView(onCreateCountdown = {
                                openSheet()
                            })
                        } else {
                            val featuredCountdowns = countdowns.filter { it.featured }
                            val otherActiveCountdowns = countdowns.filter {
                                it.expiration.minus(Clock.System.now())
                                    .isPositive() and !it.featured
                            }
                            val expiredCountdowns = countdowns.filter {
                                !it.expiration.minus(Clock.System.now()).isPositive()
                            }
                            Column(
                                Modifier
                            ) {
                                if (featuredCountdowns.isNotEmpty()) {
                                    // TODO: Consider displaying normal countdowns as featured countdowns or provide some other CTA
                                    FeaturedCountdownList(
                                        countdowns = featuredCountdowns,
                                        onCountdownSelected = onCountdownSelected,
                                    )
                                }
                                if (otherActiveCountdowns.isNotEmpty()) {
                                    ActiveCountdownsList(
                                        countdowns = otherActiveCountdowns,
                                        onCountdownSelected = onCountdownSelected,
                                        onDeleteCountdown = onDeleteCountdown,
                                        onPinCountdown = onPinCountdown,
                                        onFeatureCountdown = onFeatureCountdown,
                                    )
                                }
                                if (expiredCountdowns.isNotEmpty()) {
                                    ExpiredCountdownsList(
                                        countdowns = expiredCountdowns,
                                        onCountdownSelected = onCountdownSelected,
                                        onDeleteCountdown = onDeleteCountdown,
                                        onPinCountdown = onPinCountdown,
                                        onFeatureCountdown = onFeatureCountdown,
                                    )
                                }
                            }
                        }
                    }

                    is HomeUiState.Error -> {
                        Text("Whoops, there was an error.")
                    }

                    HomeUiState.Loading -> {
                        Text("Loading")
                    }
                }
            }
        }
    }
}


@Preview(
    name = "Empty State",
    showBackground = true,
    device = "spec:parent=pixel_7_pro",
)
@Composable
fun HomePreview() {
    ChronosTheme {
        HomeScreen(
            state = HomeUiState.Success(),
            onAddCountdown = {},
            onDeleteCountdown = {},
            onCountdownSelected = {},
            onPinCountdown = {},
            onFeatureCountdown = {},
        )
    }
}

@Preview(
    name = "Some countdowns",
    showBackground = true,
    device = "spec:parent=pixel_7_pro",
)
@Composable
fun HomePreviewWithCountdowns() {
    ChronosTheme {
        HomeScreen(
            state = HomeUiState.Success(testCountdowns),
            onAddCountdown = {},
            onDeleteCountdown = {},
            onCountdownSelected = {},
            onPinCountdown = {},
            onFeatureCountdown = {},
        )
    }
}