package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@Composable
fun PreferenceScreen(
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    TextPreference(title = {
      Text(text = stringResource(id = R.string.title_activity_oss_licenses))
    })
  }
}

@Preview
@Composable
fun PreferenceScreenPreview() {
  ValiutchikTheme {
    PreferenceScreen()
  }
}
