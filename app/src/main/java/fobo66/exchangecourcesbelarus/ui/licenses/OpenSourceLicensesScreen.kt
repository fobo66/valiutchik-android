package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.ui.EmptyListIndicator
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.core.entities.License
import fobo66.valiutchik.core.entities.OpenSourceLicensesItem

@Composable
fun OpenSourceLicensesScreen(
  licenses: List<OpenSourceLicensesItem>,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(modifier = modifier) {
    if (licenses.isEmpty()) {
      item {
        EmptyListIndicator()
      }
    } else {
      items(licenses) {
        OpenSourceLicense(item = it, onItemClick = onItemClick)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicense(
  item: OpenSourceLicensesItem,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ListItem(
    headlineText = {
      Text(text = item.project)
    },
    supportingText = {
      Column {
        Text(text = item.licenses.firstOrNull()?.license ?: "")
        Text(text = "Copyright © ${item.year.orEmpty()} ${item.developers.joinToString()}")
      }
    },
    modifier = modifier
      .clickable(enabled = item.url != null) {
        item.url?.let {
          onItemClick(it)
        }
      }
  )
}

@Preview(showBackground = true)
@Composable
fun OpenSourceLicensePreview() {
  ValiutchikTheme {
    OpenSourceLicense(
      item = OpenSourceLicensesItem(
        dependency = "androidx.activity:activity-compose:1.6.0-alpha05",
        description = "Compose integration with Activity",
        developers = listOf("The Android Open Source Project"),
        licenses = listOf(
          License(
            "The Apache Software License, Version 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0.txt"
          )
        ),
        project = "Activity Compose",
        version = "1.6.0-alpha05",
        url = null,
        year = null
      ),
      onItemClick = {}
    )
  }
}

@Preview(showBackground = true)
@Composable
fun OpenSourceLicensesPreview() {
  ValiutchikTheme {
    OpenSourceLicensesScreen(licenses = emptyList(), onItemClick = {})
  }
}