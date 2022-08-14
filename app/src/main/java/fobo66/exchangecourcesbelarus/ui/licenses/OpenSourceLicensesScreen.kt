package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState

@Composable
fun OpenSourceLicensesScreen(modifier: Modifier = Modifier) {
  val webViewState = rememberWebViewState("file:///android_asset/open_source_licenses.html")
  WebView(webViewState, modifier)
}
