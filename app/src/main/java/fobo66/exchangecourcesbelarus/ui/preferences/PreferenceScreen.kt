package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.array
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

const val MIN_UPDATE_INTERVAL_VALUE = 1f
const val MAX_UPDATE_INTERVAL_VALUE = 24f
const val UPDATE_INTERVAL_STEPS = 24

@Composable
fun PreferenceScreen(
  defaultCityValue: String,
  updateIntervalValue: Float,
  onDefaultCityChange: (String) -> Unit,
  onUpdateIntervalChange: (Float) -> Unit,
  onOpenSourceLicensesClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val citiesKeys = stringArrayResource(id = array.pref_cities_list)
  val citiesValues = stringArrayResource(id = array.pref_cities_values)
  val entries = remember {
    citiesKeys.mapIndexed { index, key -> key to citiesValues[index] }.toMap()
  }

  Column(modifier = modifier) {
    ListPreference(title = {
      Text(text = stringResource(id = R.string.pref_title_default_city))
    }, value = defaultCityValue, entries = entries, onValueChange = onDefaultCityChange)
    SeekBarPreference(
      title = {
        Text(text = stringResource(id = R.string.pref_title_update_interval))
      },
      value = updateIntervalValue,
      valueRange = MIN_UPDATE_INTERVAL_VALUE..MAX_UPDATE_INTERVAL_VALUE,
      steps = UPDATE_INTERVAL_STEPS,
      onValueChange = onUpdateIntervalChange
    )
    TextPreference(title = {
      Text(text = stringResource(id = R.string.title_activity_oss_licenses))
    },
    onClick = onOpenSourceLicensesClick)
  }
}

private const val PREVIEW_UPDATE_INTERVAL_VALUE = 3f

@Preview(name = "Preferences", showBackground = true)
@Composable
fun PreferenceScreenPreview() {
  ValiutchikTheme {
    PreferenceScreen("Минск", PREVIEW_UPDATE_INTERVAL_VALUE, {}, {}, {})
  }
}
