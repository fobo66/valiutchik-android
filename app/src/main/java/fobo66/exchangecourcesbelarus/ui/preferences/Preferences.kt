package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import fobo66.exchangecourcesbelarus.entities.Preference.PreferenceItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TextPreference(
  preference: PreferenceItem<*>,
  modifier: Modifier = Modifier,
  onClick: (() -> Unit)? = null,
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
      secondaryText = { Text(text = summaryProvider() ?: preference.summary) },
      icon = preference.icon,
      modifier = modifier.clickable(onClick = { if (isEnabled) onClick?.invoke() }),
      trailing = trailing,
    )
  }
}

@Composable
fun EnabledPreference(enabled: Boolean = true, content: @Composable () -> Unit) {
  CompositionLocalProvider(LocalContentAlpha provides if (enabled) ContentAlpha.high else ContentAlpha.disabled) {
    content()
  }
}
