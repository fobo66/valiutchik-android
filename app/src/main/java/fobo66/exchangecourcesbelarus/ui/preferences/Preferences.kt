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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import fobo66.exchangecourcesbelarus.entities.Preference.PreferenceItem
import fobo66.exchangecourcesbelarus.entities.Preference.PreferenceItem.ListPreference
import fobo66.exchangecourcesbelarus.entities.Preference.PreferenceItem.SeekBarPreference

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextPreference(
  preference: PreferenceItem<*>,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
  summary: @Composable (() -> Unit)? = null,
  summaryProvider: () -> String? = { null },
  trailing: @Composable (() -> Unit)? = null,
) {
  val isEnabled = LocalPreferenceEnabledStatus.current && preference.enabled

  EnabledPreference(isEnabled) {
    ListItem(
      text = {
        Text(
          text = preference.title,
          maxLines = if (preference.singleLineTitle) 1 else Int.MAX_VALUE
        )
      },
      secondaryText = summary ?: { Text(text = summaryProvider() ?: preference.summary) },
      icon = preference.icon,
      modifier = modifier.clickable(onClick = { if (isEnabled) onClick?.invoke() }),
      trailing = trailing,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ListPreference(
  preference: ListPreference,
  value: String,
  onValueChange: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val (isDialogShown, showDialog) = remember { mutableStateOf(false) }

  TextPreference(
    preference = preference,
    summaryProvider = { preference.entries[value] },
    onClick = { showDialog(!isDialogShown) },
    modifier = modifier
  )

  if (isDialogShown) {
    AlertDialog(
      onDismissRequest = { showDialog(false) },
      title = { Text(text = preference.title) },
      text = {
        Column(
          modifier = Modifier
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 16.dp)
        ) {
          preference.entries.forEach { current ->
            val isSelected = value == current.key
            val onSelected = {
              onValueChange(current.key)
              showDialog(false)
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
                text = current.value,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 16.dp)
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
}

@Composable
internal fun SeekBarPreference(
  preference: SeekBarPreference,
  value: Float,
  onValueChange: (Float) -> Unit,
) {
  val currentValue = remember(value) { mutableStateOf(value) }

  TextPreference(
    preference = preference,
    summary = {
      SeekbarPreferenceSummary(
        preference = preference,
        sliderValue = currentValue.value,
        onValueChange = { currentValue.value = it },
        onValueChangeEnd = { onValueChange(currentValue.value) }
      )
    }
  )
}

@Composable
private fun SeekbarPreferenceSummary(
  preference: SeekBarPreference,
  sliderValue: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeEnd: () -> Unit,
) {
  Column {
    Text(text = preference.summary)
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(text = preference.valueRepresentation(sliderValue))
      Spacer(modifier = Modifier.width(16.dp))
      Slider(
        value = sliderValue,
        onValueChange = { if (preference.enabled) onValueChange(it) },
        valueRange = preference.valueRange,
        steps = preference.steps,
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
