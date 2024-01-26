package com.craft.apps.countdowns.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.craft.apps.countdowns.core.ui.R
import com.craft.apps.countdowns.ui.common.EventLinkButton
import com.craft.apps.countdowns.ui.common.UiEventData
import com.craft.apps.countdowns.ui.theme.ChronosTypography
import com.craft.apps.countdowns.ui.theme.Spacing
import com.craft.apps.countdowns.ui.util.formattedLongDate
import com.craft.apps.countdowns.ui.util.formattedShortTime
import com.craft.apps.countdowns.util.isAfterNow
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

enum class StepperState(val value: Int) {
    START(0), BASIC_INFO(1), DETAILS(2), CUSTOMIZATION(3);
}

fun defaultInitialExpiration(daysOffset: Int = 2, minutesOffset: Int = 15) =
    Clock.System.now()
        .plus(daysOffset, DateTimeUnit.DAY, TimeZone.currentSystemDefault())
        .plus(minutesOffset, DateTimeUnit.MINUTE)

const val DEFAULT_COUNTDOWN_TITLE = "Something cool"

/**
 * Content for a sheet or dialog that allows for a new countdown to be created.
 *
 * @param initialTitle This is the default title for the countdown.
 * @param initialExpiration The starting expiration time for the countdown. By default, this is 7 days from the current time.
 * @param linkedEvent If this is present, it will be used to populate (and override the content of) its respective fields.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountdownCreationSheet(
    onTriggerCreation: (
        label: String,
        expiration: Instant,
        notes: String,
    ) -> Unit,
    initialTitle: String = DEFAULT_COUNTDOWN_TITLE,
    initialExpiration: Instant = defaultInitialExpiration(),
    initialStepperState: StepperState = StepperState.START,
    linkedEvent: UiEventData? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialExpiration.toEpochMilliseconds(),
    )
    val timePickerState = rememberTimePickerState(
        initialHour = initialExpiration.toLocalDateTime(TimeZone.currentSystemDefault()).hour,
        initialMinute = initialExpiration.toLocalDateTime(TimeZone.currentSystemDefault()).minute,
    )
    var creationStep by remember { mutableStateOf(initialStepperState) }
    var allowNext by remember { mutableStateOf(initialTitle.isNotEmpty()) }
    var titleText by remember { mutableStateOf(initialTitle) }
    var notes by remember { mutableStateOf("") }

    fun reset() {
        // TODO: Figure out how to better reset dialog state
        titleText = initialTitle
        datePickerState.selectedDateMillis = initialExpiration.toEpochMilliseconds()
        creationStep = StepperState.START
    }

    fun triggerCountdownCreation() {
        val selectedMillis = datePickerState.selectedDateMillis ?: return
        timePickerState.runCatching {
            onTriggerCreation(
                titleText,
                Instant.fromEpochMilliseconds(selectedMillis).plus(hour, DateTimeUnit.HOUR)
                    .plus(minute, DateTimeUnit.MINUTE),
                notes,
            )
            reset()
        }
    }

    val context = LocalContext.current
    fun handleAddEvent() {
        // TODO: Allow event to be associated with countdown
        Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
    }

    fun handleUnlinkEvent(eventId: String) {
        // TODO: Allow event to be disassociated from countdown
        Toast.makeText(context, "Coming soon!", Toast.LENGTH_SHORT).show()
    }

    fun handleBack() {
        when (creationStep) {
            StepperState.START -> {
                Log.i("CountdownCreation", "Attempted to go back when stepper was at start")
            }

            StepperState.BASIC_INFO -> {
                creationStep = StepperState.START
            }

            StepperState.DETAILS -> {
                creationStep = StepperState.BASIC_INFO
            }

            StepperState.CUSTOMIZATION -> {
                creationStep = StepperState.DETAILS
            }
        }

    }

    fun handleNext() {
        when (creationStep) {
            StepperState.START -> {
                creationStep = StepperState.BASIC_INFO
            }

            StepperState.BASIC_INFO -> {
                creationStep = StepperState.DETAILS
            }

            StepperState.DETAILS -> {
                creationStep = StepperState.CUSTOMIZATION
            }

            StepperState.CUSTOMIZATION -> {
                triggerCountdownCreation()
            }
        }
    }

    fun handleDateChanged(newExpiration: Instant) {
        allowNext = newExpiration.isAfterNow()
    }

    val shouldAllowTitleEdit = (linkedEvent == null) and (creationStep < StepperState.BASIC_INFO)

    LaunchedEffect(Unit) {
        // TODO: Figure out what should be the correct linkedEvent here
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(Spacing.xl),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            Column(
                Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(Spacing.lg),
            ) {
                if (creationStep < StepperState.BASIC_INFO) {
                    Text(
                        text = stringResource(R.string.label_countdown_creation_title),
                        style = ChronosTypography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                BasicTextField(
                    value = titleText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
//                    keyboardActions = KeyboardActions(onDone = { focusRequester.requestFocus() }),
                    onValueChange = { titleText = it },
                    maxLines = 3,
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                    textStyle = ChronosTypography.displaySmall.copy(MaterialTheme.colorScheme.onSurface),
                    enabled = shouldAllowTitleEdit,
                )
            }
        }
        if (creationStep > StepperState.START) {
            BasicInfoBlock(
                datePickerState,
                timePickerState,
                onDateChanged = ::handleDateChanged,
            )
        }
        EventLinkButton(
            onAddEvent = ::handleAddEvent,
            onUnlinkEvent = ::handleUnlinkEvent,
            modifier = Modifier.fillMaxWidth(),
            eventData = linkedEvent,
        )
        if (creationStep > StepperState.BASIC_INFO) {
            NotesBlock(notes = notes, onNotesUpdate = { notes = it })
        }
        if (creationStep > StepperState.DETAILS) {
            CustomizationBlock()
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                ::handleBack, enabled = creationStep > StepperState.START
            ) {
                val text = stringResource(R.string.button_countdown_creation_previous)
                Text(text)
            }
            Button(
                ::handleNext,
                enabled = allowNext,
            ) {
                val text = if (creationStep != StepperState.CUSTOMIZATION) {
                    stringResource(R.string.button_countdown_creation_next)
                } else {
                    stringResource(R.string.button_countdown_creation_finish)
                }
                Text(text)
            }
        }
    }
}

@Composable
private fun NotesBlock(
    notes: String,
    allowEdit: Boolean = true,
    onNotesUpdate: (notes: String) -> Unit,
) {
    Column {
        BasicTextField(
            value = notes,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onNotesUpdate,
            maxLines = 4,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            textStyle = ChronosTypography.displaySmall.copy(MaterialTheme.colorScheme.onSurface),
            enabled = allowEdit,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BasicInfoBlock(
    datePickerState: DatePickerState,
    timePickerState: TimePickerState,
    modifier: Modifier = Modifier,
    onDateChanged: (newExpiration: Instant) -> Unit = {}, // TODO: Actually implement callback
) {
    var dateDialogOpen by remember { mutableStateOf(false) }
    var timeDialogOpen by remember { mutableStateOf(false) }

    val millis = datePickerState.selectedDateMillis
    val date = if (millis == null) {
        // TODO: Make this hint more obvious
        "Choose a date"
    } else {
        Instant.fromEpochMilliseconds(millis).formattedLongDate()
    }
    val time = run {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        // Let's hope this doesn't bite me in the butt later
        val dt = LocalDateTime(
            now.year,
            now.monthNumber,
            now.dayOfMonth,
            timePickerState.hour,
            timePickerState.minute
        )
        dt.formattedShortTime()
    }

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
    Column(
        modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(Spacing.xl)
    ) {
        Text(
            stringResource(R.string.label_countdown_creation_expiration),
            style = ChronosTypography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                stringResource(R.string.label_countdown_date),
                style = ChronosTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                date,
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        dateDialogOpen = true
                    },
                style = ChronosTypography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(Spacing.sm),
        ) {
            Text(
                stringResource(R.string.label_countdown_time),
                style = ChronosTypography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                time,
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        timeDialogOpen = true
                    },
                style = ChronosTypography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }

    }
}

@Composable
private fun CustomizationBlock() {

}

@Preview(showBackground = true)
@Composable
private fun CountdownCreationSheetPreview() {
    CountdownCreationSheet(onTriggerCreation = { _, _, _ -> })
}

@Preview(showBackground = true)
@Composable
private fun CountdownCreationSheetWithDatePreview() {
    CountdownCreationSheet(
        onTriggerCreation = { _, _, _ -> },
        initialStepperState = StepperState.BASIC_INFO,
    )
}

@Preview(showBackground = true)
@Composable
private fun CountdownCreationSheetWithDetailsPreview() {
    CountdownCreationSheet(
        onTriggerCreation = { _, _, _ -> },
        initialStepperState = StepperState.DETAILS,
    )
}

@Preview(showBackground = true)
@Composable
private fun CountdownCreationSheetWithCustomizationPreview() {
    CountdownCreationSheet(
        onTriggerCreation = { _, _, _ -> },
        initialStepperState = StepperState.DETAILS,
    )
}
