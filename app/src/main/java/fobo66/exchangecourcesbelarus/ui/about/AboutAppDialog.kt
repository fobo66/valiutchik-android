package fobo66.exchangecourcesbelarus.ui.about

import android.R
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@Composable
fun AboutAppDialog(onDismiss: () -> Unit, modifier: Modifier = Modifier) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(text = stringResource(id = string.title_about))
    },
    text = {
      Text(text = stringResource(id = string.about_app_description))
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text(text = stringResource(id = R.string.ok))
      }
    },
    modifier = modifier
  )
}

@Preview
@Composable
fun AboutAppPreview() {
  ValiutchikTheme {
    AboutAppDialog(onDismiss = {})
  }
}
