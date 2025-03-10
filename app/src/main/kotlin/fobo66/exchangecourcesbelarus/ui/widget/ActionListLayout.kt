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

package fobo66.exchangecourcesbelarus.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.ExperimentalGlanceApi
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.action
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.components.TitleBar
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.MainActivity
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.GRID_SIZE
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.circularCornerRadius
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.itemContentSpacing
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.stateIconBackgroundSize
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.stateIconSize
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.verticalSpacing
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutDimensions.widgetPadding
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutSize.Companion.showTitleBar
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutSize.Large
import fobo66.exchangecourcesbelarus.ui.widget.ActionListLayoutSize.Small

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
fun ActionListLayout(
  title: String,
  @DrawableRes titleIconRes: Int,
  @DrawableRes titleBarActionIconRes: Int,
  titleBarActionIconContentDescription: String,
  titleBarAction: Action,
  items: List<ActionListItem>,
  actionButtonClick: (String) -> Unit,
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
      }
    )
  }

  val scaffoldTopPadding = if (showTitleBar()) {
    0.dp
  } else {
    widgetPadding
  }

  Scaffold(
    backgroundColor = GlanceTheme.colors.widgetBackground,
    modifier = modifier.padding(top = scaffoldTopPadding),
    titleBar = if (showTitleBar()) {
      titleBar()
    } else {
      null
    },
  ) {
    Content(
      items = items,
      actionButtonOnClick = actionButtonClick,
    )
  }
}

@Composable
private fun Content(
  items: List<ActionListItem>,
  actionButtonOnClick: (String) -> Unit,
) {
  val actionListLayoutSize = ActionListLayoutSize.fromLocalSize()

  Box(modifier = GlanceModifier.padding(bottom = widgetPadding)) {
    if (items.isEmpty()) {
      EmptyListContent()
    } else {
      when (actionListLayoutSize) {
        Large -> GridView(
          items = items,
          actionButtonOnClick = actionButtonOnClick
        )

        else -> ListView(
          items = items,
          actionButtonOnClick = actionButtonOnClick
        )
      }
    }
  }
}

@Composable
private fun ListView(
  items: List<ActionListItem>,
  actionButtonOnClick: (String) -> Unit,
) {
  RoundedScrollingLazyColumn(
    modifier = GlanceModifier.fillMaxSize(),
    items = items,
    verticalItemsSpacing = verticalSpacing,
    itemContentProvider = { item ->
      FilledActionListItem(
        item = item,
        actionButtonClick = actionButtonOnClick,
        modifier = GlanceModifier.fillMaxSize()
      )
    }
  )
}

@Composable
private fun GridView(
  items: List<ActionListItem>,
  actionButtonOnClick: (String) -> Unit,
) {
  RoundedScrollingLazyVerticalGrid(
    gridCells = GRID_SIZE,
    items = items,
    cellSpacing = itemContentSpacing,
    itemContentProvider = { item ->
      FilledActionListItem(
        item = item,
        actionButtonClick = actionButtonOnClick,
        modifier = GlanceModifier.fillMaxSize()
      )
    },
    modifier = GlanceModifier.fillMaxSize(),
  )
}

/**
 * A filled list / grid item that displays a title, a supporting text, and a trailing state icon.
 *
 * Uses single line title (1-2 words), and 2-line supporting text (~ 50-55 characters)
 */
@OptIn(ExperimentalGlanceApi::class)
@Composable
private fun FilledActionListItem(
  item: ActionListItem,
  actionButtonClick: (String) -> Unit,
  modifier: GlanceModifier = GlanceModifier,
) {
  ListItem(
    modifier = modifier
      // We set a combined content description on list item since entire item is clickable.
      .semantics { contentDescription = combinedContentDescription(item) }
      .filledContainer()
      .clickable(key = "${LocalSize.current} ${item.key}") { actionButtonClick(item.key) },
    contentSpacing = itemContentSpacing,
    leadingContent = takeComposableIf(ActionListLayoutSize.fromLocalSize() != Small) {
      Box(
        GlanceModifier
          .size(stateIconBackgroundSize)
          .cornerRadius(circularCornerRadius),
        contentAlignment = Alignment.Center
      ) {
        Image(
          provider = ImageProvider(R.drawable.ic_currency_exchange),
          modifier = GlanceModifier.size(stateIconSize),
          contentDescription = null, // already covered in list item container's description
          colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurfaceVariant)
        )
      }
    },
    headlineContent = {
      Text(
        text = item.title,
        style = ActionListLayoutTextStyles.titleText(),
        maxLines = 1,
        // Container's content description already reads this text
        modifier = GlanceModifier.semantics { contentDescription = "" }
      )
    },
    supportingContent = {
      Text(
        text = item.offSupportingText,
        style = ActionListLayoutTextStyles.supportingText(false),
        maxLines = 2,
        // Container's content description already reads this text
        modifier = GlanceModifier.semantics { contentDescription = "" }
      )
    },
    trailingContent = if (item.trailingIconButtonRes != null) {
      {
        CircleIconButton(
          imageProvider = ImageProvider(item.trailingIconButtonRes),
          contentDescription = item.trailingIconButtonContentDescription,
          onClick = actionStartActivity<MainActivity>(),
          backgroundColor = null,
          contentColor = GlanceTheme.colors.onSurface
        )
      }
    } else {
      null
    }
  )
}

/**
 * Returns a combined content description that can be set on entire list item.
 */
private fun combinedContentDescription(item: ActionListItem): String = buildString {
  append(item.title)
  append(" ")
  append(item.offSupportingText)
  append(" ")
  append(item.offStartActionContentDescription)
}

/** Returns the provided [block] composable if [predicate] is true, else returns null */
@Composable
private inline fun takeComposableIf(
  predicate: Boolean,
  crossinline block: @Composable () -> Unit,
): (@Composable () -> Unit)? {
  return if (predicate) {
    { block() }
  } else {
    null
  }
}

/**
 * Converts an item into a filled container by applying the background color, padding and an
 * appropriate corner radius.
 */
@Composable
private fun GlanceModifier.filledContainer(): GlanceModifier {
  return cornerRadius(ActionListLayoutDimensions.filledItemCornerRadius)
    .padding(ActionListLayoutDimensions.filledItemPadding)
    .background(GlanceTheme.colors.secondaryContainer)
}

/**
 * Holds data corresponding to each item in a
 * [ActionListLayout].
 *
 * @param key a unique identifier for a specific item
 * @param title a short text (1-2 words) representing the item
 * @param onSupportingText a compact text (~20 characters) that supports the [title] and provides
 *                         textual indication of "ON" state of item; this allows keeping the title
 *                         short and glanceable.
 * @param offSupportingText a compact text (~20 characters) that supports the [title] and provides
 *  *                         textual indication of "OFF" state of item; this allows keeping the title
 *  *                         short and glanceable.
 * @param stateIconRes a tintable icon that can represents the item and that can be presented on
 *                        a colored background to indicate the current state (e.g. bulb ON / OFF).
 *                        On click of this icon, the item's state will be toggled.
 * @param onStateActionContentDescription text to describe what happens on click of the list item
 *                                        when item is in "ON" (checked) state; appended to the
 *                                        title and supporting text when used by the the
 *                                        accessibility services.
 * @param offStartActionContentDescription text to describe what happens on click of the list item
 *                                        when item is in "OFF" (un-checked) state; appended to the
 *                                        title and supporting text when used by the the
 *                                        accessibility services.
 */
data class ActionListItem(
  val key: String,
  val title: String,
  val onSupportingText: String,
  val offSupportingText: String,
  @DrawableRes val stateIconRes: Int,
  val onStateActionContentDescription: String,
  val offStartActionContentDescription: String,
  @DrawableRes val trailingIconButtonRes: Int? = null,
  val trailingIconButtonContentDescription: String? = null,
)

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
  Large(maxWidth = 644.dp);

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
    fun showTitleBar(): Boolean {
      return LocalSize.current.height >= 180.dp
    }
  }
}

private object ActionListLayoutTextStyles {
  /**
   * Style for the text displayed as title within each item.
   */
  @Composable
  fun titleText(): TextStyle = TextStyle(
    fontWeight = FontWeight.Medium,
    fontSize = if (ActionListLayoutSize.fromLocalSize() == Small) {
      14.sp // M3 Title Small
    } else {
      16.sp // M3 Title Medium
    },
    color = GlanceTheme.colors.onSurface
  )

  /**
   * Style for the text displayed as supporting text within each item.
   */
  @Composable
  fun supportingText(isChecked: Boolean): TextStyle =
    TextStyle(
      fontWeight = FontWeight.Normal,
      fontSize = 12.sp, // M3 Label Medium
      color = if (isChecked) {
        GlanceTheme.colors.onPrimary
      } else {
        GlanceTheme.colors.onSurfaceVariant
      }
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

/**
 * Preview sizes for the widget covering the breakpoints.
 *
 * This allows verifying updates across multiple breakpoints.
 */
@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 259, heightDp = 200)
@Preview(widthDp = 438, heightDp = 200)
@Preview(widthDp = 644, heightDp = 200)
private annotation class PreviewActionListBreakpoints

/**
 * Previews for the action list layout.
 *
 * First we look at the previews at defined breakpoints, tweaking them as necessary. In addition,
 * the previews at standard sizes allows us to quickly verify updates across min / max and common
 * widget sizes without needing to run the app or manually place the widget.
 */
@PreviewActionListBreakpoints
@PreviewSmallWidget
@PreviewMediumWidget
@PreviewLargeWidget
@Composable
private fun ActionListLayoutPreview() {
  ActionListLayout(
    title = "Test",
    titleIconRes = R.drawable.ic_launcher_foreground,
    titleBarActionIconRes = R.drawable.ic_launcher_foreground,
    titleBarActionIconContentDescription = "test",
    titleBarAction = action {},
    items = emptyList(),
    actionButtonClick = {},
  )
}
