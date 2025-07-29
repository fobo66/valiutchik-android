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

package dev.fobo66.valiutchik.ui

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.onChild
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.runComposeUiTest
import androidx.test.filters.SmallTest
import dev.fobo66.valiutchik.presentation.entity.LicensesState
import dev.fobo66.valiutchik.ui.licenses.OpenSourceLicensesScreen
import fobo66.valiutchik.domain.entities.OpenSourceLicense
import kotlin.test.Test
import kotlinx.collections.immutable.persistentListOf

@SmallTest
@OptIn(ExperimentalTestApi::class)
class LicensesScreenTest {
    @Test
    fun showLoading() = runComposeUiTest {
        setContent {
            OpenSourceLicensesScreen(
                licensesState = LicensesState(persistentListOf()),
                onBackClick = {},
                onItemClick = {}
            )
        }
        onNodeWithTag(TAG_PROGRESS).assertIsDisplayed()
    }

    @Test
    fun showLicensesList() = runComposeUiTest {
        setContent {
            OpenSourceLicensesScreen(
                licensesState = LicensesState(
                    persistentListOf(
                        OpenSourceLicense(
                            "project",
                            "test",
                            "2025",
                            "authors"
                        )
                    )
                ),
                onBackClick = {},
                onItemClick = {}
            )
        }
        onNodeWithTag(TAG_LICENSES_LIST)
            .onChild()
            .assertIsDisplayed()
    }

    @Test
    fun canClickWhenUrlIsAvailable() = runComposeUiTest {
        setContent {
            OpenSourceLicensesScreen(
                licensesState = LicensesState(
                    persistentListOf(
                        OpenSourceLicense(
                            "project",
                            "test",
                            "2025",
                            "authors",
                            "test"
                        )
                    )
                ),
                onBackClick = {},
                onItemClick = {}
            )
        }
        onNodeWithTag(TAG_LICENSES_LIST)
            .onChild()
            .assertHasClickAction()
    }

    @Test
    fun cannotClickWhenNoUrl() = runComposeUiTest {
        setContent {
            OpenSourceLicensesScreen(
                licensesState = LicensesState(
                    persistentListOf(
                        OpenSourceLicense(
                            "project",
                            "test",
                            "2025",
                            "authors"
                        )
                    )
                ),
                onBackClick = {},
                onItemClick = {}
            )
        }
        onNodeWithTag(TAG_LICENSES_LIST)
            .onChild()
            .assertIsNotEnabled()
    }
}
