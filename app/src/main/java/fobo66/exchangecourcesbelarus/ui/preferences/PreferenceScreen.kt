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

@Composable
fun PreferenceScreen(
  defaultCityValue: String,
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
    }, value = defaultCityValue, entries = entries, onValueChange = {})
    TextPreference(title = {
      Text(text = stringResource(id = R.string.title_activity_oss_licenses))
    })
  }
}

@Preview(name = "Preferences", showBackground = true)
@Composable
fun PreferenceScreenPreview() {
  ValiutchikTheme {
    PreferenceScreen("Минск")
  }
}
