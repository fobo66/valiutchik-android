package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
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
  val isEnabled = LocalPreferenceEnabledStatus.current && enabled

  EnabledPreference(isEnabled) {
    ListItem(
      headlineText = title,
      supportingText = summary ?: {
        Text(
          text = summaryProvider(),
          style = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
          )
        )
      },
      modifier = modifier.clickable(onClick = { if (isEnabled) onClick?.invoke() }),
      trailingContent = trailing
    )
  }
}

@Composable
fun ListPreference(
  title: @Composable () -> Unit,
  value: String,
  entries: Map<String, String>,
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
      val summaryValue = entries.entries.find { it.value == value }?.key
      Text(
        text = summaryValue ?: entries.keys.first(),
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface
        )
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

@Composable
@OptIn(ExperimentalComposeUiApi::class)
private fun ListPreferenceDialog(
  onDismiss: () -> Unit,
  title: @Composable () -> Unit,
  entries: Map<String, String>,
  value: String,
  onValueChange: (String) -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = title,
    text = {
      Column(
        modifier = Modifier
          .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
      ) {
        entries.forEach { current ->
          val isSelected = value == current.value
          val onSelected = {
            onValueChange(current.value)
            onDismiss()
          }
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .fillMaxWidth()
              .selectable(
                selected = isSelected,
                onClick = { if (!isSelected) onSelected() }
              )
              .padding(4.dp)
          ) {
            RadioButton(
              selected = isSelected,
              onClick = { if (!isSelected) onSelected() }
            )
            Text(
              text = current.key,
              style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.onSurface
              ),
              modifier = Modifier.padding(start = 8.dp)
            )
          }
        }
      }
    },
    properties = DialogProperties(
      usePlatformDefaultWidth = true
    ),
    confirmButton = { }
  )
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
  val currentValue = remember(value) { mutableStateOf(value) }

  TextPreference(
    title = title,
    modifier = modifier,
    enabled = enabled,
    summary = {
      SeekbarPreferenceSummary(
        enabled = enabled,
        sliderValue = currentValue.value,
        valueRepresentation = { it.roundToInt().toString() },
        onValueChange = { currentValue.value = it },
        onValueChangeEnd = { onValueChange(currentValue.value) },
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
        style = MaterialTheme.typography.bodyMedium.copy(
          color = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier.width(24.dp)
      )
      Spacer(modifier = Modifier.width(16.dp))
      Slider(
        value = sliderValue,
        onValueChange = { if (enabled) onValueChange(it) },
        valueRange = valueRange,
        steps = steps,
        onValueChangeFinished = onValueChangeEnd
      )
    }
  }
}

@Composable
fun EnabledPreference(enabled: Boolean = true, content: @Composable () -> Unit) {
  CompositionLocalProvider(
    LocalContentAlpha provides if (enabled) {
      ContentAlpha.high
    } else {
      ContentAlpha.disabled
    }
  ) {
    content()
  }
}
