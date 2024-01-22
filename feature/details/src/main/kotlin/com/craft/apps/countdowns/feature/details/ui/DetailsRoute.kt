/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.craft.apps.countdowns.feature.details.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DetailsRoute(
    onGoBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    viewModel.logView()
    DetailScreen(state, onGoBack, modifier)
}

//@Composable
//internal fun DetailsScreen(
//    state: DetailsUiState,
//    onGoBack: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    when (state) {
//        DetailsUiState.Loading -> Loading(modifier)
//        is DetailsUiState.Success -> Content(state, onGoBack, modifier)
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//internal fun Content(
//    state: DetailsUiState.Success,
//    onGoBack: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = { Text(state.title) },
//                navigationIcon = {
//                    IconButton(onGoBack, modifier = Modifier.testTag("nav_icon")) {
//                        Icon(Icons.Default.ArrowBack, null)
//                    }
//                }
//            )
//        },
//        modifier = modifier
//    ) { padding ->
//        Text(state.description, Modifier.padding(padding))
//    }
//}
