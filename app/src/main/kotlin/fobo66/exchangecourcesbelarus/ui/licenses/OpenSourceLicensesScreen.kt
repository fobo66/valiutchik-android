/*
 *    Copyright 2025 Andrey Mukamolov
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import dev.fobo66.valiutchik.presentation.entity.LicensesState
import dev.fobo66.valiutchik.ui.element.ProgressIndicator
import dev.fobo66.valiutchik.ui.element.SecondaryTopBar
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import fobo66.valiutchik.domain.entities.OpenSourceLicense
import kotlinx.collections.immutable.persistentListOf

@Composable
fun OpenSourceLicensesScreen(
    licensesState: LicensesState,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SecondaryTopBar(
            title = stringResource(R.string.title_activity_oss_licenses),
            onBackClick = onBackClick
        )
        Crossfade(
            targetState = licensesState,
            label = "licenses",
            modifier = Modifier.weight(1f)
        ) { state ->
            if (state.licenses.isEmpty()) {
                ProgressIndicator()
            } else {
                LazyColumn {
                    items(licensesState.licenses) { item ->
                        OpenSourceLicense(
                            item = item,
                            onItemClick = onItemClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OpenSourceLicense(
    item: OpenSourceLicense,
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
                Text(
                    text = buildAnnotatedString {
                        append("Copyright © ")
                        if (item.year.isNotEmpty()) {
                            append(item.year)
                            append(' ')
                        }
                        append(item.authors)
                    }
                )
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
            item = OpenSourceLicense(
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
        OpenSourceLicensesScreen(
            licensesState = LicensesState(persistentListOf()),
            onItemClick = {},
            onBackClick = {}
        )
    }
}
