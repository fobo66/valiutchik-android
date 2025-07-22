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
import androidx.annotation.StringRes
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
import dev.fobo66.valiutchik.android.widget.ActionListLayout
import dev.fobo66.valiutchik.android.widget.PreviewLargeWidget
import dev.fobo66.valiutchik.android.widget.PreviewMediumWidget
import dev.fobo66.valiutchik.android.widget.PreviewSmallWidget
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CurrencyWidget :
    GlanceAppWidget(),
    KoinComponent {
    private val loadExchangeRates: LoadExchangeRates by inject()
    private val refreshExchangeRates: ForceRefreshExchangeRates by inject()

    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId): Nothing {
        val ratesState =
            loadExchangeRates
                .execute()
                .map { it.toImmutableList() }

        provideContent {
            val scope = rememberCoroutineScope()
            val rates by ratesState
                .collectAsState(
                    initial = persistentListOf()
                )

            ValiutchikWidgetTheme {
                CurrencyWidgetContent(
                    rates = rates,
                    onTitleBarActionClick = {
                        scope.launch {
                            refreshExchangeRates.execute()
                            update(context, id)
                        }
                    }
                )
            }
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        val ratesState =
            loadExchangeRates
                .execute()
                .map { it.toImmutableList() }

        provideContent {
            ValiutchikWidgetTheme {
                val rates by ratesState.collectAsState(initial = persistentListOf())
                CurrencyWidgetContent(
                    rates = rates,
                    onTitleBarActionClick = {}
                )
            }
        }
    }
}

@Composable
fun CurrencyWidgetContent(
    rates: ImmutableList<BestCurrencyRate>,
    onTitleBarActionClick: () -> Unit,
    modifier: GlanceModifier = GlanceModifier
) {
    val context = LocalContext.current

    ActionListLayout(
        title = context.getString(R.string.app_name),
        titleIconRes = R.drawable.ic_widget_logo,
        titleBarActionIconRes = R.drawable.ic_refresh,
        titleBarActionIconContentDescription = context.getString(R.string.widget_action_refresh),
        titleBarAction = action(null, onTitleBarActionClick),
        items = rates,
        actionButtonClick = actionStartActivity<MainActivity>(),
        itemHeadlineTextProvider = { context.getString(resolveCurrencyName()) },
        itemMainTextProvider = { rateValue },
        itemSupportingTextProvider = { bank },
        emptyListContent = { EmptyListContent() },
        supportingTextIcon = R.drawable.ic_bank,
        supportingTextIconDescription = context.getString(R.string.bank_name_indicator),
        leadingIcon = R.drawable.ic_currency_exchange,
        trailingIcon = R.drawable.ic_open_in_app,
        trailingIconDescription = context.getString(R.string.open_app),
        modifier = modifier
    )
}

@StringRes
private fun BestCurrencyRate.resolveCurrencyName(): Int = when (this) {
    is BestCurrencyRate.DollarBuyRate -> R.string.currency_name_usd_buy
    is BestCurrencyRate.DollarSellRate -> R.string.currency_name_usd_sell
    is BestCurrencyRate.EuroBuyRate -> R.string.currency_name_eur_buy
    is BestCurrencyRate.EuroSellRate -> R.string.currency_name_eur_sell
    is BestCurrencyRate.HryvniaBuyRate -> R.string.currency_name_uah_buy
    is BestCurrencyRate.HryvniaSellRate -> R.string.currency_name_uah_sell
    is BestCurrencyRate.ZlotyBuyRate -> R.string.currency_name_pln_buy
    is BestCurrencyRate.ZlotySellRate -> R.string.currency_name_pln_sell
    is BestCurrencyRate.RubleBuyRate -> R.string.currency_name_rub_buy
    is BestCurrencyRate.RubleSellRate -> R.string.currency_name_rub_sell
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
                BestCurrencyRate.DollarBuyRate(
                    bank = "test",
                    rateValue = "1.23"
                ),
                BestCurrencyRate.DollarSellRate(
                    bank = "test",
                    rateValue = "4.56"
                )
            ),
            onTitleBarActionClick = {}
        )
    }
}
