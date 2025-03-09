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

import androidx.compose.runtime.Composable
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
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.components.FilledButton
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.MainActivity

/**
 * Content to be displayed when there are no items in the list. To be displayed below the
 * app-specific title bar in the [androidx.glance.appwidget.components.Scaffold] .
 */
@Composable
fun EmptyListContent() {
  val context = LocalContext.current

  NoDataContent(
    noDataText = context.getString(R.string.no_rates_indicator),
    noDataIconRes = R.drawable.ic_empty_rates,
    actionButtonText = context.getString(R.string.open_app),
    actionButtonIcon = R.drawable.ic_launcher_foreground,
    actionButtonOnClick = actionStartActivity<MainActivity>()
  )
}

/**
 * Component for a view that can be shown when the app has no data to present.
 *
 * The content should be displayed in a [androidx.glance.appwidget.components.Scaffold] below an app-specific
 * title bar.
 *
 * @param noDataText text indicating that there is no data available to display.
 * @param noDataIconRes a tintable icon indicating there is no data available to display; usually
 *                      a crossed-out icon representing the data that is empty; e.g. a crossed out
 *                      message icon if there were no messages.
 * @param actionButtonText text for the button that performs specific operation when there is no
 *                         data; e.g. sign-in button, add button, etc.
 * @param actionButtonIcon a leading icon to be displayed within the action button.
 * @param actionButtonOnClick action to be performed on click of the action button.
 */
@Composable
internal fun NoDataContent(
  noDataIconRes: Int,
  noDataText: String,
  actionButtonText: String,
  actionButtonIcon: Int,
  actionButtonOnClick: Action,
) {
  @Composable
  fun showIcon() = LocalSize.current.height >= 180.dp

  Column(
    verticalAlignment = Alignment.CenterVertically,
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = GlanceModifier.fillMaxSize()
  ) {
    if (showIcon()) {
      Image(
        provider = ImageProvider(noDataIconRes),
        colorFilter = ColorFilter.tint(GlanceTheme.colors.secondary),
        contentDescription = null, // only decorative
      )
      Spacer(modifier = GlanceModifier.height(8.dp))
    }
    Text(
      text = noDataText,
      style = TextStyle(
        fontWeight = FontWeight.Medium,
        color = GlanceTheme.colors.onSurface,
        fontSize = 16.sp // M3 - title/medium
      )
    )
    Spacer(modifier = GlanceModifier.height(8.dp))
    FilledButton(
      text = actionButtonText,
      icon = ImageProvider(actionButtonIcon),
      onClick = actionButtonOnClick
    )
  }
}
