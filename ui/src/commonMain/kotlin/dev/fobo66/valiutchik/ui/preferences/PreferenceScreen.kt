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

package dev.fobo66.valiutchik.ui.preferences

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import dev.fobo66.valiutchik.ui.TAG_DEFAULT_CITY
import dev.fobo66.valiutchik.ui.TAG_LICENSES
import dev.fobo66.valiutchik.ui.TAG_PREFERENCES
import dev.fobo66.valiutchik.ui.TAG_UPDATE_INTERVAL
import dev.fobo66.valiutchik.ui.element.SecondaryTopBar
import dev.fobo66.valiutchik.ui.entities.ListPreferenceEntries
import dev.fobo66.valiutchik.ui.entities.ListPreferenceEntry
import dev.fobo66.valiutchik.ui.theme.AppTheme
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import valiutchik.ui.generated.resources.Res
import valiutchik.ui.generated.resources.pref_cities_list
import valiutchik.ui.generated.resources.pref_cities_values
import valiutchik.ui.generated.resources.pref_title_default_city
import valiutchik.ui.generated.resources.pref_title_update_interval
import valiutchik.ui.generated.resources.title_activity_oss_licenses
import valiutchik.ui.generated.resources.title_activity_settings

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
                title = stringResource(Res.string.title_activity_settings),
                onBackClick = onBackClick
            )
        }
        PreferenceScreenContent(
            defaultCityValue = defaultCityValue,
            updateIntervalValue = updateIntervalValue,
            onDefaultCityChange = onDefaultCityChange,
            onUpdateIntervalChange = onUpdateIntervalChange,
            onOpenSourceLicensesClick = onOpenSourceLicensesClick
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
    val citiesKeys = stringArrayResource(Res.array.pref_cities_list)
    val citiesValues = stringArrayResource(Res.array.pref_cities_values)
    val entries = remember {
        ListPreferenceEntries(
            citiesKeys.mapIndexed { index, key -> ListPreferenceEntry(key, citiesValues[index]) }
                .toImmutableList()
        )
    }

    Column(
        modifier = modifier.testTag(TAG_PREFERENCES)
    ) {
        ListPreference(
            title = {
                Text(text = stringResource(Res.string.pref_title_default_city))
            },
            value = defaultCityValue,
            entries = entries,
            onValueChange = onDefaultCityChange,
            modifier = Modifier.testTag(TAG_DEFAULT_CITY)
        )
        SeekBarPreference(
            title = {
                Text(text = stringResource(Res.string.pref_title_update_interval))
            },
            value = updateIntervalValue,
            valueRange = MIN_UPDATE_INTERVAL_VALUE..MAX_UPDATE_INTERVAL_VALUE,
            steps = UPDATE_INTERVAL_STEPS,
            onValueChange = onUpdateIntervalChange,
            modifier = Modifier.testTag(TAG_UPDATE_INTERVAL)
        )
        TextPreference(
            title = {
                Text(text = stringResource(Res.string.title_activity_oss_licenses))
            },
            onClick = onOpenSourceLicensesClick,
            modifier = Modifier.testTag(TAG_LICENSES)
        )
    }
}

private const val PREVIEW_UPDATE_INTERVAL_VALUE = 3f

@Preview
@Composable
private fun PreferenceScreenPreview() {
    AppTheme {
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
