@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class,
)

package com.craft.apps.countdowns.core.ui.home

import android.util.Log
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.craft.apps.countdowns.core.data.repository.Countdown
import com.craft.apps.countdowns.core.theme.LogDateTheme
import com.craft.apps.countdowns.core.ui.common.CountdownCreationSheet
import com.craft.apps.countdowns.core.ui.common.CountdownList
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant

@Composable
fun HomeScreen(
    onCountdownSelected: (countdownId: Int) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    var showBottomSheet by remember { mutableStateOf(false) }
    val homeUiState by viewModel.uiState.collectAsState()

    fun onActionClick() {

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

    fun handleCountdownCreation(label: String, timestamp: Instant) {
        val countdown = Countdown(0, label, timestamp)
        viewModel.addCountdown(countdown)
        hideSheet()
    }

    fun handleCountdownDeletion(countdownId: Int) {
        viewModel.removeCountdown(countdownId)
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetContent = {
            CountdownCreationSheet(onTriggerCreation = ::handleCountdownCreation)
        },
    ) {
        Scaffold(
            floatingActionButton = {
                ExtendedFloatingActionButton(text = { Text("New countdown") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                    onClick = {
                        openSheet()
                    })
            },
            topBar = {
                TopAppBar(
                    title = { Text("Countdowns") },
                    actions = {
                        IconButton(onClick = { onActionClick() }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    },
                )
            },
        ) { contentPadding ->
            Box(modifier = Modifier.padding(contentPadding)) {
                when (homeUiState) {
                    is LatestHomeUiState.Success -> {
                        val countdowns = (homeUiState as LatestHomeUiState.Success).countdowns
                        if (countdowns.isEmpty()) {
                            HomeEmptyView(onCreateCountdown = {
                                openSheet()
                            })
                        } else {
                            CountdownList(
                                countdowns = countdowns,
                                onCountdownSelected = onCountdownSelected,
                                onCountdownDeletion = ::handleCountdownDeletion,
                            )
                        }
                    }

                    is LatestHomeUiState.Error -> {
                        Text("Whoops, there was an error.")
                    }

                    LatestHomeUiState.Loading -> {
                        Text("Loading")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomePreview() {
    LogDateTheme {
        HomeScreen(onCountdownSelected = {})
    }
}