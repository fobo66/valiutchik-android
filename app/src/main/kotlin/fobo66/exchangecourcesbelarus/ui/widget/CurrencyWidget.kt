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

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.action
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.provideContent
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.MainActivity
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikWidgetTheme
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.ForceRefreshExchangeRates
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrencyWidget :
  GlanceAppWidget(),
  KoinComponent {
  private val loadExchangeRates: LoadExchangeRates by inject()
  private val refreshExchangeRates: ForceRefreshExchangeRates by inject()

  override val sizeMode: SizeMode
    get() = SizeMode.Exact

  override suspend fun provideGlance(
    context: Context,
    id: GlanceId,
  ): Nothing {
    val ratesState =
      loadExchangeRates
        .execute(Clock.System.now())
        .map { it.toImmutableList() }

    provideContent {
      val scope = rememberCoroutineScope()
      val rates by ratesState
        .collectAsState(
          initial = persistentListOf(),
        )

      ValiutchikWidgetTheme {
        CurrencyWidgetContent(
          rates = rates,
          onTitleBarActionClick = {
            scope.launch {
              refreshExchangeRates.execute(Clock.System.now())
              update(context, id)
            }
          },
        )
      }
    }
  }
}

@Composable
fun CurrencyWidgetContent(
  rates: ImmutableList<BestCurrencyRate>,
  onTitleBarActionClick: () -> Unit,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  ActionListLayout(
    title = context.getString(R.string.app_name),
    titleIconRes = R.drawable.ic_launcher_foreground,
    titleBarActionIconRes = R.drawable.ic_refresh,
    titleBarActionIconContentDescription = context.getString(R.string.widget_action_refresh),
    titleBarAction = action(null, onTitleBarActionClick),
    items = rates,
    actionButtonClick = actionStartActivity<MainActivity>(),
    modifier = modifier,
  )
}

class CurrencyAppWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget
    get() = CurrencyWidget()
}

@PreviewSmallWidget
@PreviewMediumWidget
@PreviewLargeWidget
@Composable
private fun CurrencyWidgetPreview() {
  ValiutchikWidgetTheme {
    CurrencyWidgetContent(
      rates =
        persistentListOf(
          BestCurrencyRate(
            0,
            "test",
            fobo66.valiutchik.domain.R.string.currency_name_eur_buy,
            "1.23",
          ),
          BestCurrencyRate(
            1,
            "test",
            fobo66.valiutchik.domain.R.string.currency_name_eur_sell,
            "1.23",
          ),
        ),
      onTitleBarActionClick = {},
    )
  }
}
