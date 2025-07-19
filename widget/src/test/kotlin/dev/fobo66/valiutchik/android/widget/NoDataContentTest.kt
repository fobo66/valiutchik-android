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

package dev.fobo66.valiutchik.android.widget

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.glance.action.action
import androidx.glance.appwidget.testing.unit.runGlanceAppWidgetUnitTest
import androidx.glance.testing.unit.hasTestTag
import org.junit.jupiter.api.Test

class NoDataContentTest {
    @Test
    fun `icon is shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(200.dp, 200.dp))

        provideComposable {
            NoDataContent(
                noDataIconRes = 0,
                noDataText = "test",
                actionButtonText = "action",
                actionButtonIcon = 0,
                actionButtonOnClick = action { }
            )
        }

        onNode(hasTestTag(TAG_NO_DATA_ICON)).assertExists()
    }
}
