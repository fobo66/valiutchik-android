/*
 *    Copyright 2023 Andrey Mukamolov
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.preferencesOf
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.ExperimentalGlanceRemoteViewsApi
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.Column
import androidx.glance.text.Text
import com.google.android.glance.appwidget.host.glance.GlanceAppWidgetHostPreview
import dagger.hilt.android.AndroidEntryPoint
import fobo66.exchangecourcesbelarus.ui.theme.ValiutchikWidgetTheme
import fobo66.valiutchik.domain.R
import fobo66.valiutchik.domain.entities.BestCurrencyRate
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CurrencyWidget(private val loadExchangeRates: LoadExchangeRates) : GlanceAppWidget() {
  override suspend fun provideGlance(context: Context, id: GlanceId) = provideContent {
    val rates by loadExchangeRates.execute(LocalDateTime.now()).collectAsState(
      initial = persistentListOf()
    )

    ValiutchikWidgetTheme {
      LazyColumn(
        modifier = GlanceModifier
          .appWidgetBackground()
          .cornerRadius(16.dp)
      ) {
        items(rates, { item -> item.id }) {
          Column {
            Text(text = context.getString(it.currencyNameRes))
            Text(text = it.currencyValue)
            Text(text = it.bank)
          }
        }
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

@OptIn(ExperimentalGlanceRemoteViewsApi::class)
@Preview
@Composable
private fun MyAppWidgetPreview() {
  val loadExchangeRates: LoadExchangeRates = object : LoadExchangeRates {
    override fun execute(now: LocalDateTime): Flow<List<BestCurrencyRate>> =
      flowOf(
        persistentListOf(
          BestCurrencyRate(
            1,
            "test",
            R.string.currency_name_eur_buy,
            "3.003"
          )
        )
      )
  }
  // The size of the widget
  val displaySize = DpSize(400.dp, 400.dp)
  // Your GlanceAppWidget instance
  val instance = CurrencyWidget(loadExchangeRates)
  // Provide a state depending on the GlanceAppWidget state definition
  val state = preferencesOf()

  GlanceAppWidgetHostPreview(
    modifier = Modifier.fillMaxSize(),
    glanceAppWidget = instance,
    state = state,
    displaySize = displaySize,
  )
}
