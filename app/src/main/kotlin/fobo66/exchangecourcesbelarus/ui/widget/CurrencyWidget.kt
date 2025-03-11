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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Column
import androidx.glance.layout.padding
import androidx.glance.preview.ExperimentalGlancePreviewApi
import androidx.glance.preview.Preview
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
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
  LazyColumn(
    modifier =
      modifier
        .background(GlanceTheme.colors.widgetBackground)
        .appWidgetBackground()
        .cornerRadius(16.dp),
  ) {
    items(rates, { item -> item.id }) {
      Column(modifier = GlanceModifier.padding(16.dp)) {
        Text(
          text = stringResource(it.currencyNameRes),
          style = TextStyle(color = GlanceTheme.colors.primary),
        )
        Text(
          text = it.currencyValue,
          style =
            TextStyle(
              color = GlanceTheme.colors.primary,
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold,
            ),
        )
        Text(text = it.bank, style = TextStyle(color = GlanceTheme.colors.primary))
      }
    }
  }
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
      BestCurrencyRate(0, "test", fobo66.valiutchik.domain.R.string.currency_name_eur_buy, "1.23"),
    ),
  )
}
