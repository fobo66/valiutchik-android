/*
 *    Copyright 2024 Andrey Mukamolov
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
import androidx.compose.ui.unit.dp
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
import androidx.glance.text.Text
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikWidgetTheme
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import javax.inject.Inject
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock

class CurrencyWidget(private val loadExchangeRates: LoadExchangeRates) : GlanceAppWidget() {
  override suspend fun provideGlance(context: Context, id: GlanceId) = provideContent {
    val rates: ImmutableList<BestCurrencyRate> by loadExchangeRates.execute(Clock.System.now())
      .map { it.toImmutableList() }
      .collectAsState(
        initial = persistentListOf()
      )

    ValiutchikWidgetTheme {
      CurrencyWidgetContent(context = context, rates = rates)
    }
  }
}

@Composable
fun CurrencyWidgetContent(
  context: Context,
  rates: ImmutableList<BestCurrencyRate>,
  modifier: GlanceModifier = GlanceModifier
) {
  LazyColumn(
    modifier = modifier
      .background(GlanceTheme.colors.background)
      .appWidgetBackground()
      .cornerRadius(16.dp)
  ) {
    items(rates, { item -> item.id }) {
      Column(modifier = GlanceModifier.padding(16.dp)) {
        Text(text = context.getString(it.currencyNameRes))
        Text(text = it.currencyValue)
        Text(text = it.bank)
      }
    }
  }
}

@AndroidEntryPoint
class CurrencyAppWidgetReceiver : GlanceAppWidgetReceiver() {

  @Inject
  lateinit var loadExchangeRates: LoadExchangeRates

  override val glanceAppWidget: GlanceAppWidget
    get() = CurrencyWidget(loadExchangeRates)
}
