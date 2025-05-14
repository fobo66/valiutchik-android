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

package fobo66.exchangecourcesbelarus.ui.preferences

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.R.array
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.entities.ListPreferenceEntries
import fobo66.exchangecourcesbelarus.entities.ListPreferenceEntry
import fobo66.exchangecourcesbelarus.ui.TAG_DEFAULT_CITY
import fobo66.exchangecourcesbelarus.ui.TAG_LICENSES
import fobo66.exchangecourcesbelarus.ui.TAG_PREFERENCES
import fobo66.exchangecourcesbelarus.ui.TAG_UPDATE_INTERVAL
import fobo66.exchangecourcesbelarus.ui.main.SecondaryTopBar
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikTheme
import kotlinx.collections.immutable.toImmutableList

const val MIN_UPDATE_INTERVAL_VALUE = 1f
const val MAX_UPDATE_INTERVAL_VALUE = 24f
const val UPDATE_INTERVAL_STEPS = 22

@Composable
fun PreferenceScreen(
    defaultCityValue: String,
    updateIntervalValue: Float,
    canOpenSettings: Boolean,
    onDefaultCityChange: (String) -> Unit,
    onUpdateIntervalChange: (Float) -> Unit,
    onOpenSourceLicensesClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        this.AnimatedVisibility(canOpenSettings) {
            SecondaryTopBar(
                title = stringResource(string.title_activity_settings),
                onBackClick = onBackClick
            )
        }
        PreferenceScreenContent(
            defaultCityValue = defaultCityValue,
            updateIntervalValue = updateIntervalValue,
            onDefaultCityChange = onDefaultCityChange,
            onUpdateIntervalChange = onUpdateIntervalChange,
            onOpenSourceLicensesClick = onOpenSourceLicensesClick,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PreferenceScreenContent(
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
        ListPreferenceEntries(
            citiesKeys.mapIndexed { index, key -> ListPreferenceEntry(key, citiesValues[index]) }
                .toImmutableList()
        )
    }

    Column(
        modifier = modifier
            .testTag(TAG_PREFERENCES)
            .background(MaterialTheme.colorScheme.background)
    ) {
        ListPreference(
            title = {
                Text(text = stringResource(id = R.string.pref_title_default_city))
            },
            value = defaultCityValue,
            entries = entries,
            onValueChange = onDefaultCityChange,
            modifier = Modifier.testTag(TAG_DEFAULT_CITY)
        )
        SeekBarPreference(
            title = {
                Text(text = stringResource(id = R.string.pref_title_update_interval))
            },
            value = updateIntervalValue,
            valueRange = MIN_UPDATE_INTERVAL_VALUE..MAX_UPDATE_INTERVAL_VALUE,
            steps = UPDATE_INTERVAL_STEPS,
            onValueChange = onUpdateIntervalChange,
            modifier = Modifier.testTag(TAG_UPDATE_INTERVAL)
        )
        TextPreference(
            title = {
                Text(text = stringResource(id = R.string.title_activity_oss_licenses))
            },
            onClick = onOpenSourceLicensesClick,
            modifier = Modifier.testTag(TAG_LICENSES)
        )
    }
}

private const val PREVIEW_UPDATE_INTERVAL_VALUE = 3f

@Preview(name = "Preferences", showBackground = true)
@Composable
private fun PreferenceScreenPreview() {
    ValiutchikTheme {
        PreferenceScreen(
            defaultCityValue = "Minsk",
            updateIntervalValue = PREVIEW_UPDATE_INTERVAL_VALUE,
            canOpenSettings = true,
            onDefaultCityChange = {},
            onUpdateIntervalChange = {},
            onOpenSourceLicensesClick = {},
            onBackClick = {}
        )
    }
}
