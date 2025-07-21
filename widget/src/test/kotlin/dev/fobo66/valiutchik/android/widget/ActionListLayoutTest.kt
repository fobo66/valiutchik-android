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
import androidx.glance.testing.unit.hasText
import androidx.glance.text.Text
import kotlinx.collections.immutable.persistentListOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import tech.apter.junit.jupiter.robolectric.RobolectricExtension

@ExtendWith(RobolectricExtension::class)
class ActionListLayoutTest {
    @Test
    fun `title is shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(200.dp, 200.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1", "test2"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = {},
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_TITLE_BAR)).assertExists()
    }

    @Test
    fun `title is not shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(150.dp, 150.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1", "test2"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = {},
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_TITLE_BAR)).assertDoesNotExist()
    }

    @Test
    fun `empty content shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(150.dp, 150.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf<String>(),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasText("Empty")).assertExists()
    }

    @Test
    fun `list shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(150.dp, 150.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1", "test2"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_LIST)).assertExists()
    }

    @Test
    fun `grid shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(500.dp, 200.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1", "test2"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_GRID)).assertExists()
    }

    @Test
    fun `trailing icon shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(500.dp, 200.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_TRAILING_ICON)).assertExists()
    }

    @Test
    fun `leading icon shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(500.dp, 200.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_LEADING_ICON)).assertExists()
    }

    @Test
    fun `supporting icon shown`() = runGlanceAppWidgetUnitTest {
        setAppWidgetSize(DpSize(500.dp, 200.dp))

        provideComposable {
            ActionListLayout(
                title = "test",
                titleIconRes = 0,
                titleBarActionIconRes = 0,
                titleBarActionIconContentDescription = "action",
                titleBarAction = action { },
                items = persistentListOf("test1"),
                actionButtonClick = action { },
                itemHeadlineTextProvider = { this },
                itemMainTextProvider = { this },
                itemSupportingTextProvider = { this },
                emptyListContent = { Text("Empty") },
                supportingTextIcon = 0,
                supportingTextIconDescription = "support",
                leadingIcon = 0,
                trailingIcon = 0,
                trailingIconDescription = "trailing"
            )
        }

        onNode(hasTestTag(TAG_SUPPORTING_ICON)).assertExists()
    }
}
