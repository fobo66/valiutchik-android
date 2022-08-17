package fobo66.exchangecourcesbelarus.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.about.AboutAppDialog
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesScreen
import fobo66.exchangecourcesbelarus.ui.licenses.OpenSourceLicensesViewModel
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme

@AndroidEntryPoint
class NewMainActivity : ComponentActivity() {
  @OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      val navController = rememberNavController()

      var isAboutDialogShown by remember {
        mutableStateOf(false)
      }

      ValiutchikTheme {
        Scaffold(topBar = {
          SmallTopAppBar(title = {
            Text(text = stringResource(id = R.string.app_name))
          }, actions = {
              IconButton(onClick = {
                isAboutDialogShown = true
              }) {
                Icon(
                  painterResource(id = R.drawable.ic_about),
                  contentDescription = stringResource(
                    id = R.string.action_about
                  )
                )
              }
            })
        }) {
          NavHost(
            navController = navController,
            startDestination = "prefs",
            modifier = Modifier.padding(it)
          ) {
            preferenceScreen(navController)
            composable("licenses") {
              val openSourceLicensesViewModel: OpenSourceLicensesViewModel = hiltViewModel()

              val licenses by openSourceLicensesViewModel.licenses.collectAsStateWithLifecycle(
                initialValue = emptyList()
              )

              OpenSourceLicensesScreen(licenses = licenses, onItemClick = { licenseUrl ->
                startActivity(Intent(Intent.ACTION_VIEW, licenseUrl.toUri()))
              })
            }
          }
          if (isAboutDialogShown) {
            AboutAppDialog(
              onDismiss = {
                isAboutDialogShown = false
              }
            )
          }
        }
      }
    }
  }
}
