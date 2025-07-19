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
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.actionStartActivity
import dev.fobo66.valiutchik.android.widget.NoDataContent
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.MainActivity

/**
 * Content to be displayed when there are no items in the list. To be displayed below the
 * app-specific title bar in the [androidx.glance.appwidget.components.Scaffold] .
 */
@Composable
fun EmptyListContent(modifier: GlanceModifier = GlanceModifier) {
    val context = LocalContext.current

    NoDataContent(
        noDataText = context.getString(R.string.no_rates_indicator),
        noDataIconRes = R.drawable.ic_empty_rates,
        actionButtonText = context.getString(R.string.open_app),
        actionButtonIcon = R.drawable.ic_open_in_app,
        actionButtonOnClick = actionStartActivity<MainActivity>(),
        modifier = modifier
    )
}
