/*
 *    Copyright 2025 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package dev.fobo66.valiutchik.ui.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import dev.fobo66.valiutchik.ui.TAG_SLIDER
import dev.fobo66.valiutchik.ui.entities.ListPreferenceEntries
import kotlin.math.roundToInt

@Composable
fun TextPreference(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
    summary: @Composable (() -> Unit)? = null,
    summaryProvider: () -> String = { "" },
    trailing: @Composable (() -> Unit)? = null
) {
    ListItem(
        headlineContent = title,
        supportingContent =
        summary ?: {
            Text(
                text = summaryProvider()
            )
        },
        modifier =
        modifier.clickable(onClick = {
            if (enabled) {
                onClick?.invoke()
            }
        }),
        trailingContent = trailing
    )
}

@Composable
fun ListPreference(
    title: @Composable () -> Unit,
    value: String,
    entries: ListPreferenceEntries,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onValueChange: (String) -> Unit
) {
    val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

    TextPreference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        onClick = { showDialog(!isDialogShown) },
        summary = {
            val summaryValue = entries.preferenceEntries.find { it.value == value }?.key
            Text(
                text = summaryValue ?: entries.preferenceEntries.first().key
            )
        }
    )

    if (isDialogShown) {
        ListPreferenceDialog(
            onDismiss = { showDialog(false) },
            title = title,
            entries = entries,
            value = value,
            onValueChange = onValueChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPreferenceDialog(
    onDismiss: () -> Unit,
    title: @Composable () -> Unit,
    entries: ListPreferenceEntries,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier =
            modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    shape = MaterialTheme.shapes.large
                )
                .padding(24.dp)
        ) {
            ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
                title()
            }
            LazyColumn(contentPadding = PaddingValues(top = 16.dp)) {
                items(items = entries.preferenceEntries) { current ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = current.key
                            )
                        },
                        leadingContent = {
                            RadioButton(
                                selected = value == current.value,
                                modifier =
                                Modifier.semantics {
                                    stateDescription = current.key
                                },
                                onClick = {
                                    if (value != current.value) {
                                        onValueChange(current.value)
                                        onDismiss()
                                    }
                                }
                            )
                        },
                        colors =
                        ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        ),
                        modifier =
                        Modifier
                            .clickable(onClick = {
                                if (value != current.value) {
                                    onValueChange(current.value)
                                    onDismiss()
                                }
                            })
                            .semantics {
                                stateDescription = current.key
                            }
                    )
                }
            }
        }
    }
}

@Composable
internal fun SeekBarPreference(
    title: @Composable () -> Unit,
    value: Float,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0,
    onValueChange: (Float) -> Unit
) {
    val currentValue = remember(value) { mutableFloatStateOf(value) }

    TextPreference(
        title = title,
        modifier = modifier,
        enabled = enabled,
        summary = {
            SeekbarPreferenceSummary(
                enabled = enabled,
                sliderValue = currentValue.floatValue,
                valueRepresentation = { it.roundToInt().toString() },
                onValueChange = { currentValue.floatValue = it },
                onValueChangeEnd = { onValueChange(currentValue.floatValue) },
                valueRange = valueRange,
                steps = steps
            )
        }
    )
}

@Composable
private fun SeekbarPreferenceSummary(
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeEnd: () -> Unit,
    valueRepresentation: (Float) -> String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = valueRepresentation(sliderValue),
                modifier = Modifier.width(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Slider(
                enabled = enabled,
                value = sliderValue,
                onValueChange = onValueChange,
                valueRange = valueRange,
                steps = steps,
                onValueChangeFinished = onValueChangeEnd,
                modifier = Modifier.testTag(TAG_SLIDER)
            )
        }
    }
}
