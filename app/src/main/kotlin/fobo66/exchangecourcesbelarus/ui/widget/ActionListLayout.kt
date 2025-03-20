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
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.Action
import androidx.glance.action.action
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
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.semantics.contentDescription
import androidx.glance.semantics.semantics
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import fobo66.exchangecourcesbelarus.R
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
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import io.github.aakira.napier.Napier
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

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
  items: ImmutableList<BestCurrencyRate>,
  actionButtonClick: Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  fun titleBar(): @Composable (() -> Unit) =
    {
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
            onClick = titleBarAction,
          )
        },
      )
    }
  Napier.d(LocalSize.current.toString())

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
      },
  ) {
    Content(
      items = items,
      actionButtonClick = actionButtonClick,
    )
  }
}

@Composable
private fun Content(
  items: ImmutableList<BestCurrencyRate>,
  actionButtonClick: Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  val actionListLayoutSize = ActionListLayoutSize.fromLocalSize()
  Napier.d(actionListLayoutSize.name)

  Box(modifier = modifier.padding(bottom = widgetPadding)) {
    if (items.isEmpty()) {
      EmptyListContent()
    } else {
      when (actionListLayoutSize) {
        Large ->
          GridView(
            items = items,
            actionButtonClick = actionButtonClick,
          )

        else ->
          ListView(
            items = items,
            actionButtonClick = actionButtonClick,
          )
      }
    }
  }
}

@Composable
private fun ListView(
  items: ImmutableList<BestCurrencyRate>,
  actionButtonClick: Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  RoundedScrollingLazyColumn(
    modifier = modifier.fillMaxSize(),
    items = items,
    verticalItemsSpacing = verticalSpacing,
    itemContentProvider = { item ->
      CurrencyListItem(
        currencyName = context.getString(item.currencyNameRes),
        currencyValue = item.currencyValue,
        bankName = item.bank,
        actionButtonClick = actionButtonClick,
        modifier = GlanceModifier.fillMaxSize(),
      )
    },
  )
}

@Composable
private fun GridView(
  items: ImmutableList<BestCurrencyRate>,
  actionButtonClick: Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  RoundedScrollingLazyVerticalGrid(
    gridCells = GRID_SIZE,
    items = items,
    cellSpacing = itemContentSpacing,
    itemContentProvider = { item ->
      CurrencyListItem(
        currencyName = context.getString(item.currencyNameRes),
        currencyValue = item.currencyValue,
        bankName = item.bank,
        actionButtonClick = actionButtonClick,
        modifier = GlanceModifier.fillMaxSize(),
      )
    },
    modifier = modifier.fillMaxSize(),
  )
}

@Composable
private fun CurrencyListItem(
  currencyName: String,
  currencyValue: String,
  bankName: String,
  actionButtonClick: Action,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  ListItem(
    modifier =
      modifier
        // We set a combined content description on list item since entire item is clickable.
        .semantics {
          contentDescription = combinedContentDescription(currencyName, currencyValue, bankName)
        }.filledContainer(),
    contentSpacing = itemContentSpacing,
    leadingContent =
      takeComposableIf(ActionListLayoutSize.fromLocalSize() != Small) {
        Box(
          GlanceModifier
            .size(stateIconBackgroundSize)
            .cornerRadius(circularCornerRadius),
          contentAlignment = Alignment.Center,
        ) {
          Image(
            provider = ImageProvider(R.drawable.ic_currency_exchange),
            modifier = GlanceModifier.size(stateIconSize),
            contentDescription = null, // already covered in list item container's description
            colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurfaceVariant),
          )
        }
      },
    headlineContent = {
      Text(
        text = currencyName,
        style = ActionListLayoutTextStyles.titleText(),
        maxLines = 1,
        // Container's content description already reads this text
        modifier = GlanceModifier.semantics { contentDescription = "" },
      )
    },
    supportingContent = {
      CurrencyValueContent(currencyValue, bankName)
    },
    trailingContent = {
      CircleIconButton(
        imageProvider = ImageProvider(R.drawable.ic_open_in_app),
        contentDescription = context.getString(R.string.open_map),
        onClick = actionButtonClick,
        backgroundColor = null,
        contentColor = GlanceTheme.colors.onSurface,
      )
    },
  )
}

@Composable
private fun CurrencyValueContent(
  currencyValue: String,
  bankName: String,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  Column(modifier = modifier) {
    Text(
      text = currencyValue,
      maxLines = 2,
      style = ActionListLayoutTextStyles.mainText(),
      // Container's content description already reads this text
      modifier = GlanceModifier.semantics { contentDescription = "" },
    )
    Row(
      verticalAlignment = Alignment.CenterVertically,
    ) {
      Image(
        provider = ImageProvider(R.drawable.ic_bank),
        colorFilter = ColorFilter.tint(GlanceTheme.colors.onSurface),
        contentDescription = context.getString(R.string.bank_name_indicator),
      )
      Text(
        text = bankName,
        style = ActionListLayoutTextStyles.supportingText(),
        modifier = GlanceModifier.padding(start = 8.dp),
      )
    }
  }
}

/**
 * Returns a combined content description that can be set on entire list item.
 */
private fun combinedContentDescription(
  currencyName: String,
  currencyValue: String,
  bankName: String,
): String =
  buildString {
    append(currencyName)
    append(" ")
    append(currencyValue)
    append(" ")
    append(bankName)
  }

/** Returns the provided [block] composable if [predicate] is true, else returns null */
@Composable
private inline fun takeComposableIf(
  predicate: Boolean,
  crossinline block: @Composable () -> Unit,
): (@Composable () -> Unit)? =
  if (predicate) {
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
private enum class ActionListLayoutSize(
  val maxWidth: Dp,
) {
  // Single column list - compact view e.g. reduced fonts.
  Small(maxWidth = 260.dp),

  // Single column list
  Medium(maxWidth = 439.dp),

  // 2 Column Grid
  Large(maxWidth = 644.dp),
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
  fun titleText(): TextStyle =
    TextStyle(
      fontWeight = FontWeight.Medium,
      fontSize =
        if (ActionListLayoutSize.fromLocalSize() == Small) {
          14.sp // M3 Title Small
        } else {
          16.sp // M3 Title Medium
        },
      color = GlanceTheme.colors.onSurface,
    )

  /**
   * Style for the text displayed as supporting text within each item.
   */
  @Composable
  fun mainText(): TextStyle =
    TextStyle(
      color = GlanceTheme.colors.primary,
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold,
    )

  /**
   * Style for the text displayed as supporting text within each item.
   */
  @Composable
  fun supportingText(): TextStyle =
    TextStyle(
      fontWeight = FontWeight.Normal,
      fontSize = 12.sp, // M3 Label Medium
      color = GlanceTheme.colors.onSurfaceVariant,
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
    titleBarActionIconRes = R.drawable.ic_refresh,
    titleBarActionIconContentDescription = "test",
    titleBarAction = action {},
    items = persistentListOf(),
    actionButtonClick = action {},
  )
}
