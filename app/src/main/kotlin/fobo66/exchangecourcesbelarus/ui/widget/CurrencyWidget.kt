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
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.action.action
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import fobo66.exchangecourcesbelarus.R
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikWidgetTheme
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrencyWidget :
  GlanceAppWidget(),
  KoinComponent {
  private val loadExchangeRates: LoadExchangeRates by inject()

  override suspend fun provideGlance(
    context: Context,
    id: GlanceId,
  ) = provideContent {
    val rates by loadExchangeRates
      .execute(Clock.System.now())
      .collectAsState(
        initial = emptyList(),
      )

    ValiutchikWidgetTheme {
      CurrencyWidgetContent(rates = rates)
    }
  }
}

@Composable
fun CurrencyWidgetContent(
  rates: List<BestCurrencyRate>,
  modifier: GlanceModifier = GlanceModifier,
) {
  val context = LocalContext.current

  ActionListLayout(
    title = context.getString(R.string.app_name),
    titleIconRes = R.drawable.ic_launcher_foreground,
    titleBarActionIconRes = R.drawable.ic_refresh,
    titleBarActionIconContentDescription = "Refresh",
    titleBarAction = action { },
    items = rates,
    actionButtonClick = { _, _ -> },
    itemClick = {},
    modifier = modifier,
  )
}

class CurrencyAppWidgetReceiver : GlanceAppWidgetReceiver() {
  override val glanceAppWidget: GlanceAppWidget
    get() = CurrencyWidget()
}

@OptIn(ExperimentalGlancePreviewApi::class)
@Preview(widthDp = 140, heightDp = 160)
@Composable
private fun CurrencyWidgetPreview() {
  CurrencyWidgetContent(
    listOf(
      BestCurrencyRate(
        0,
        "test",
        fobo66.valiutchik.domain.R.string.currency_name_eur_buy,
        "1.23",
      ),
    ),
  )
}
