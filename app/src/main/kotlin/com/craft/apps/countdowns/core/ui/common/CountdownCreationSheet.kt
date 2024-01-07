@file:OptIn(ExperimentalMaterial3Api::class)

package com.craft.apps.countdowns.core.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.data.repository.formatted
import com.craft.apps.countdowns.theme.AppTypographyStyles
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.plus

/**
 * Content for a sheet or dialog that allows for a new countdown to be created.
 */
@Composable
fun CountdownCreationSheet(onTriggerCreation: (label: String, timestamp: Instant) -> Unit) {
    var labelText by remember {
        mutableStateOf("New countdown")
    }
    var dateDialogOpen by remember {
        mutableStateOf(false)
    }
    var timeDialogOpen by remember {
        mutableStateOf(false)
    }
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    val time = if (timePickerState.hour == 0) {
        "Choose a time"
    } else {
        // TODO: Format time properly
        "${timePickerState.hour}:${timePickerState.minute}"
    }
    val millis = datePickerState.selectedDateMillis
    val date = if (millis != null) {
        Instant.fromEpochMilliseconds(millis).formatted()
    } else {
        "Choose a date"
    }

    fun reset() {
        // TODO: Figure out how to reset dialog state
        labelText = "New countdown"
        datePickerState.setSelection(0)
    }

    fun triggerCountdownCreation() {
        val selectedMillis = datePickerState.selectedDateMillis ?: return
        timePickerState.runCatching {
            onTriggerCreation(
                labelText,
                Instant.fromEpochMilliseconds(selectedMillis)
                    .plus(hour, DateTimeUnit.HOUR)
                    .plus(minute, DateTimeUnit.MINUTE)
            )
            reset()
        }
    }

    Column(
        Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Create a new countdown",
            style = AppTypographyStyles.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
        TextField(
            value = labelText,
            onValueChange = {
                labelText = it
            },
            label = { Text("Countdown Title") },
        )
        Text(
            date,
            Modifier.clickable {
                dateDialogOpen = true
            },
            style = AppTypographyStyles.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (dateDialogOpen) {
            DatePickerDialog(
                onDismissRequest = { dateDialogOpen = false },
                confirmButton = {
                    TextButton(onClick = {
                        dateDialogOpen = false
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        dateDialogOpen = false
                    }) { Text("Cancel") }
                },
            ) {
                DatePicker(state = datePickerState)
            }
        }
        Text(
            time,
            Modifier.clickable {
                timeDialogOpen = true
            },
            style = AppTypographyStyles.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (timeDialogOpen) {
            DatePickerDialog(
                onDismissRequest = { timeDialogOpen = false },
                confirmButton = {
                    TextButton(onClick = {
                        timeDialogOpen = false
                    }) { Text("Confirm") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        timeDialogOpen = false
                    }) { Text("Cancel") }
                },
            ) {
                TimePicker(state = timePickerState)
            }
        }
        Button(onClick = ::triggerCountdownCreation) {
            Text("Create")
        }
    }
}