/*
 *    Copyright 2024 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.exchangecourcesbelarus.ui.licenses

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.entities.LicenseItem
import fobo66.exchangecourcesbelarus.entities.LicensesState
import fobo66.exchangecourcesbelarus.ui.EmptyListIndicator
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OpenSourceLicensesScreen(
  licensesState: LicensesState,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  Crossfade(licensesState, label = "licenses") {
    if (it.licenses.isEmpty()) {
      EmptyListIndicator()
    } else {
      LazyColumn(modifier = modifier) {
        items(licensesState.licenses) { item ->
          OpenSourceLicense(
            item = item,
            onItemClick = onItemClick,
            modifier = Modifier.animateItem()
          )
        }
      }
    }
  }
}

@Composable
fun OpenSourceLicense(
  item: LicenseItem,
  onItemClick: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  ListItem(
    headlineContent = {
      Text(text = item.project)
    },
    supportingContent = {
      Column {
        Text(text = item.licenses)
        Text(text = "Copyright © ${item.year} ${item.authors}")
      }
    },
    modifier = modifier
      .clickable(
        enabled = item.url != null,
        onClickLabel = stringResource(id = R.string.see_license_click_label)
      ) {
        item.url?.let {
          onItemClick(it)
        }
      }
  )
}

@Preview(showBackground = true)
@Composable
private fun OpenSourceLicensePreview() {
  ValiutchikTheme {
    OpenSourceLicense(
      item = LicenseItem(
        authors = "The Android Open Source Project",
        licenses = "The Apache Software License, Version 2.0",
        project = "Activity Compose",
        url = null,
        year = "2019"
      ),
      onItemClick = {}
    )
  }
}

@Preview(showBackground = true)
@Composable
private fun OpenSourceLicensesPreview() {
  ValiutchikTheme {
    OpenSourceLicensesScreen(licensesState = LicensesState(persistentListOf()), onItemClick = {})
  }
}
