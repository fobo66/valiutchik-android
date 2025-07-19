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

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.semantics.testTag
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.GRID_SIZE
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.circularCornerRadius
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.itemContentSpacing
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.stateIconBackgroundSize
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.stateIconSize
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.verticalSpacing
import dev.fobo66.valiutchik.android.widget.ActionListLayoutDimensions.widgetPadding
import dev.fobo66.valiutchik.android.widget.ActionListLayoutSize.Companion.showTitleBar
import dev.fobo66.valiutchik.android.widget.ActionListLayoutSize.Large
import dev.fobo66.valiutchik.android.widget.ActionListLayoutSize.Small
import kotlinx.collections.immutable.ImmutableList

/**
 * A layout focused on presenting list of two-state actions represented by a title (1-2 words),
 * supporting text (~20 characters) and a leading state icon button to indicate the current state
 * and an optional trailing icon button to perform additional operations.
 *
 * Tapping each item changes the state (e.g. turn ON / OFF). The additional actions can be used to
 * perform tuned state changes e.g. editing the temperature The layout is suitable for use cases
 * such as home control, quick settings, etc.
 *
 * The content is displayed in a [Scaffold] below an app-specific title bar.
 *
 * In larger sizes, the layout shows a multi-column grid with each item taking minimum size to
 * fit the item content. The state icon is dropped in smaller sizes to allow space for additional
 * action and the text.
 *
 * Derived from [platform sample](https://github.com/android/platform-samples/blob/main/samples/user-interface/appwidgets/src/main/java/com/example/platform/ui/appwidgets/glance/layout/collections/layout/ActionListLayout.kt)
 *
 * @param title the text to be displayed as title of the widget, e.g. name of your widget or app.
 * @param titleIconRes a tintable icon that represents your app or brand, that can be displayed
 *                     with the provided [title]. In this sample, we use icon from a drawable
 *                     resource, but you should use an appropriate icon source for your use case.
 * @param titleBarActionIconRes resource id of a tintable icon that can be displayed as
 *                              an icon button within the title bar area of the widget. For
 *                              example, a search icon to launch search for finding specific
 *                              items.
 * @param titleBarActionIconContentDescription description of the [titleBarActionIconRes] button
 *                                             to be used by the accessibility services.
 * @param titleBarAction action to be performed on click of the [titleBarActionIconRes] button.
 * @param items list of items to be included in the list; typically includes a short title, a
 *              supporting text and the action icon information.
 * @param actionButtonClick handler to toggle the state.
 */
@Composable
fun <T> ActionListLayout(
    title: String,
    @DrawableRes titleIconRes: Int,
    @DrawableRes titleBarActionIconRes: Int,
    titleBarActionIconContentDescription: String,
    titleBarAction: Action,
    items: ImmutableList<T>,
    actionButtonClick: Action,
    itemHeadlineTextProvider: T.() -> String,
    itemMainTextProvider: T.() -> String,
    itemSupportingTextProvider: T.() -> String,
    emptyListContent: @Composable () -> Unit,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    trailingIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    fun titleBar(): @Composable (() -> Unit) = {
        TitleBar(
            startIcon = ImageProvider(titleIconRes),
            title = title.takeIf { ActionListLayoutSize.fromLocalSize() != Small } ?: "",
            iconColor = GlanceTheme.colors.primary,
            textColor = GlanceTheme.colors.onSurface,
            actions = {
                CircleIconButton(
                    imageProvider = ImageProvider(titleBarActionIconRes),
                    contentDescription = titleBarActionIconContentDescription,
                    contentColor = GlanceTheme.colors.secondary,
                    backgroundColor = null, // transparent
                    onClick = titleBarAction
                )
            },
            modifier = GlanceModifier.semantics { testTag = TAG_TITLE_BAR }
        )
    }

    val scaffoldTopPadding =
        if (showTitleBar()) {
            0.dp
        } else {
            widgetPadding
        }

    Scaffold(
        backgroundColor = GlanceTheme.colors.widgetBackground,
        modifier = modifier.padding(top = scaffoldTopPadding),
        titleBar =
        if (showTitleBar()) {
            titleBar()
        } else {
            null
        }
    ) {
        Content(
            items = items,
            headlineTextProvider = itemHeadlineTextProvider,
            mainTextProvider = itemMainTextProvider,
            supportingTextProvider = itemSupportingTextProvider,
            actionButtonClick = actionButtonClick,
            emptyListContent = emptyListContent,
            supportingTextIcon = supportingTextIcon,
            supportingTextIconDescription = supportingTextIconDescription,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            trailingIconDescription = trailingIconDescription
        )
    }
}

@Composable
private fun <T> Content(
    items: ImmutableList<T>,
    actionButtonClick: Action,
    headlineTextProvider: T.() -> String,
    mainTextProvider: T.() -> String,
    supportingTextProvider: T.() -> String,
    emptyListContent: @Composable () -> Unit,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    trailingIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    val actionListLayoutSize = ActionListLayoutSize.fromLocalSize()

    Box(modifier = modifier.padding(bottom = widgetPadding)) {
        if (items.isEmpty()) {
            emptyListContent()
        } else {
            when (actionListLayoutSize) {
                Large ->
                    GridView(
                        items = items,
                        actionButtonClick = actionButtonClick,
                        headlineTextProvider = headlineTextProvider,
                        mainTextProvider = mainTextProvider,
                        supportingTextProvider = supportingTextProvider,
                        supportingTextIcon = supportingTextIcon,
                        supportingTextIconDescription = supportingTextIconDescription,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        trailingIconDescription = trailingIconDescription,
                        modifier = GlanceModifier.semantics { testTag = TAG_GRID }
                    )

                else ->
                    ListView(
                        items = items,
                        actionButtonClick = actionButtonClick,
                        headlineTextProvider = headlineTextProvider,
                        mainTextProvider = mainTextProvider,
                        supportingTextProvider = supportingTextProvider,
                        supportingTextIcon = supportingTextIcon,
                        supportingTextIconDescription = supportingTextIconDescription,
                        leadingIcon = leadingIcon,
                        trailingIcon = trailingIcon,
                        trailingIconDescription = trailingIconDescription,
                        modifier = GlanceModifier.semantics { testTag = TAG_LIST }
                    )
            }
        }
    }
}

@Composable
private fun <T> ListView(
    items: ImmutableList<T>,
    actionButtonClick: Action,
    headlineTextProvider: T.() -> String,
    mainTextProvider: T.() -> String,
    supportingTextProvider: T.() -> String,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    trailingIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    RoundedScrollingLazyColumn(
        modifier = modifier.fillMaxSize(),
        items = items,
        verticalItemsSpacing = verticalSpacing,
        itemContentProvider = { item ->
            ListItem(
                headlineText = item.headlineTextProvider(),
                mainText = item.mainTextProvider(),
                supportingText = item.supportingTextProvider(),
                actionButtonClick = actionButtonClick,
                supportingTextIcon = supportingTextIcon,
                supportingTextIconDescription = supportingTextIconDescription,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                trailingIconDescription = trailingIconDescription,
                modifier = GlanceModifier.fillMaxSize()
            )
        }
    )
}

@Composable
private fun <T> GridView(
    items: ImmutableList<T>,
    actionButtonClick: Action,
    headlineTextProvider: T.() -> String,
    mainTextProvider: T.() -> String,
    supportingTextProvider: T.() -> String,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    trailingIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    RoundedScrollingLazyVerticalGrid(
        gridCells = GRID_SIZE,
        items = items,
        cellSpacing = itemContentSpacing,
        itemContentProvider = { item ->
            ListItem(
                headlineText = item.headlineTextProvider(),
                mainText = item.mainTextProvider(),
                supportingText = item.supportingTextProvider(),
                actionButtonClick = actionButtonClick,
                supportingTextIcon = supportingTextIcon,
                supportingTextIconDescription = supportingTextIconDescription,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                trailingIconDescription = trailingIconDescription,
                modifier = GlanceModifier.fillMaxSize()
            )
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun ListItem(
    headlineText: String,
    mainText: String,
    supportingText: String,
    actionButtonClick: Action,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    @DrawableRes leadingIcon: Int,
    @DrawableRes trailingIcon: Int,
    trailingIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    ListItem(
        modifier =
        modifier
            // We set a combined content description on list item since entire item is clickable.
            .semantics {
                contentDescription =
                    combinedContentDescription(headlineText, mainText, supportingText)
            }.filledContainer(),
        contentSpacing = itemContentSpacing,
        leadingContent =
        takeComposableIf(ActionListLayoutSize.fromLocalSize() != Small) {
            Box(
                GlanceModifier
                    .size(stateIconBackgroundSize)
                    .cornerRadius(circularCornerRadius),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    provider = ImageProvider(leadingIcon),
                    modifier = GlanceModifier.size(stateIconSize)
                        .semantics { testTag = TAG_LEADING_ICON },
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurfaceVariant)
                )
            }
        },
        headlineContent = {
            Text(
                text = headlineText,
                style = ActionListLayoutTextStyles.headlineText(),
                // Container's content description already reads this text
                modifier = GlanceModifier.semantics { contentDescription = "" }
            )
        },
        supportingContent = {
            MainListItemContent(
                text = mainText,
                supportingText = supportingText,
                supportingTextIcon = supportingTextIcon,
                supportingTextIconDescription = supportingTextIconDescription
            )
        },
        trailingContent = takeComposableIf(ActionListLayoutSize.fromLocalSize() != Small) {
            CircleIconButton(
                imageProvider = ImageProvider(trailingIcon),
                contentDescription = trailingIconDescription,
                onClick = actionButtonClick,
                backgroundColor = null,
                contentColor = GlanceTheme.colors.onSurface,
                modifier = GlanceModifier.semantics { testTag = TAG_TRAILING_ICON }
            )
        }
    )
}

@Composable
private fun MainListItemContent(
    text: String,
    supportingText: String,
    @DrawableRes supportingTextIcon: Int,
    supportingTextIconDescription: String,
    modifier: GlanceModifier = GlanceModifier
) {
    Column(modifier = modifier) {
        Text(
            text = text,
            maxLines = 2,
            style = ActionListLayoutTextStyles.mainText(),
            // Container's content description already reads this text
            modifier = GlanceModifier.semantics { contentDescription = "" }
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ActionListLayoutSize.fromLocalSize() != Small) {
                Image(
                    provider = ImageProvider(supportingTextIcon),
                    colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
                    contentDescription = supportingTextIconDescription,
                    modifier = GlanceModifier.padding(end = 8.dp)
                        .semantics { testTag = TAG_SUPPORTING_ICON }

                )
            }
            Text(
                text = supportingText,
                style = ActionListLayoutTextStyles.supportingText()
            )
        }
    }
}

/**
 * Returns a combined content description that can be set on entire list item.
 */
private fun combinedContentDescription(
    headlineText: String,
    mainText: String,
    supportingText: String
): String = buildString {
    append(headlineText)
    append(" ")
    append(mainText)
    append(" ")
    append(supportingText)
}

/** Returns the provided [block] composable if [predicate] is true, else returns null */
@Composable
private inline fun takeComposableIf(
    predicate: Boolean,
    crossinline block: @Composable () -> Unit
): (@Composable () -> Unit)? = if (predicate) {
    { block() }
} else {
    null
}

/**
 * Converts an item into a filled container by applying the background color, padding and an
 * appropriate corner radius.
 */
@Composable
private fun GlanceModifier.filledContainer(): GlanceModifier =
    cornerRadius(ActionListLayoutDimensions.filledItemCornerRadius)
        .padding(ActionListLayoutDimensions.filledItemPadding)
        .background(GlanceTheme.colors.secondaryContainer)

/**
 * Size of the widget per the reference breakpoints. Each size has its own display
 * characteristics such as - showing content as list vs grid, font sizes etc.
 *
 * In this layout, only width breakpoints are used to scale the layout.
 */
private enum class ActionListLayoutSize(val maxWidth: Dp) {
    // Single column list - compact view e.g. reduced fonts.
    Small(maxWidth = 260.dp),

    // Single column list
    Medium(maxWidth = 439.dp),

    // 2 Column Grid
    Large(maxWidth = 644.dp)
    ;

    companion object {
        /**
         * Returns the corresponding [ActionListLayoutSize] to be considered for the current
         * widget size.
         */
        @Composable
        fun fromLocalSize(): ActionListLayoutSize {
            val width = LocalSize.current.width

            return if (width >= Medium.maxWidth) {
                Large
            } else if (width >= Small.maxWidth) {
                Medium
            } else {
                Small
            }
        }

        @Composable
        fun showTitleBar(): Boolean = LocalSize.current.height >= 180.dp
    }
}

private object ActionListLayoutTextStyles {
    /**
     * Style for the text displayed as title within each item.
     */
    @Composable
    fun headlineText(): TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize =
        if (ActionListLayoutSize.fromLocalSize() == Small) {
            14.sp // M3 Title Small Expressive
        } else {
            28.sp // M3 Headline Medium
        },
        color = GlanceTheme.colors.onSurface
    )

    /**
     * Style for the text displayed as supporting text within each item.
     */
    @Composable
    fun mainText(): TextStyle = TextStyle(
        color = GlanceTheme.colors.primary,
        fontSize = if (ActionListLayoutSize.fromLocalSize() == Small) {
            16.sp // M3 Title Medium Expressive
        } else {
            45.sp // M3 Display Medium Expressive
        },
        fontWeight = if (ActionListLayoutSize.fromLocalSize() == Small) {
            FontWeight.Bold
        } else {
            FontWeight.Medium
        }
    )

    /**
     * Style for the text displayed as supporting text within each item.
     */
    @Composable
    fun supportingText(): TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = if (ActionListLayoutSize.fromLocalSize() == Small) {
            14.sp // M3 Body Medium
        } else {
            18.sp // M3 Headline Medium
        },
        color = GlanceTheme.colors.onSurfaceVariant
    )
}

private object ActionListLayoutDimensions {
    /** Number of cells in the grid, when items are displayed as a grid. */
    const val GRID_SIZE = 2

    /** Padding applied at bottom of the widget content */
    val widgetPadding = 12.dp

    /** Corner radius for each filled list item. */
    val filledItemCornerRadius = 16.dp

    /** Padding for filled list items. */
    val filledItemPadding = 12.dp

    /** Vertical spacing between items in the list.*/
    val verticalSpacing = 4.dp

    /**
     * Spacing between individual sections in within the list item (for instance, horizontal spacing
     * between state icon & text section.
     */
    val itemContentSpacing = 4.dp

    /** Size of the background layer on the state icon. */
    val stateIconBackgroundSize = 48.dp

    /** Size of the state icon image. */
    val stateIconSize = 24.dp

    /**  Corner radius to achieve circular shape. */
    val circularCornerRadius = 200.dp
}
