package fobo66.exchangecourcesbelarus.model.usecases

import androidx.annotation.StringRes
import fobo66.exchangecourcesbelarus.R.string
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.CurrencyName
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.BestCurrencyRate
import fobo66.valiutchik.core.usecases.LoadExchangeRates
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoadExchangeRatesImpl @Inject constructor(
  private val currencyRateRepository: CurrencyRateRepository
) : LoadExchangeRates {
  override fun execute(): Flow<List<BestCurrencyRate>> =
    currencyRateRepository.loadExchangeRates()
      .map {
        it.map { bestCourse ->
          @StringRes val currencyNameRes =
            resolveCurrencyName(bestCourse.currencyName, bestCourse.isBuy)

          bestCourse.toBestCurrencyRate(currencyNameRes)
        }
      }

  @StringRes
  private fun resolveCurrencyName(@CurrencyName currencyName: String, isBuy: Boolean) =
    when (currencyName to isBuy) {
      USD to true -> string.currency_name_usd_buy
      USD to false -> string.currency_name_usd_sell
      EUR to true -> string.currency_name_eur_buy
      EUR to false -> string.currency_name_eur_sell
      RUB to true -> string.currency_name_rub_buy
      RUB to false -> string.currency_name_rub_sell
      RUR to true -> string.currency_name_rub_buy
      RUR to false -> string.currency_name_rub_sell
      else -> 0
    }
}
