package fobo66.valiutchik.domain.usecases

import androidx.annotation.StringRes
import fobo66.valiutchik.api.CurrencyName
import fobo66.valiutchik.api.EUR
import fobo66.valiutchik.api.RUB
import fobo66.valiutchik.api.RUR
import fobo66.valiutchik.api.USD
import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.model.repository.CurrencyRateRepository
import fobo66.valiutchik.domain.R
import fobo66.valiutchik.domain.entities.BestCurrencyRate
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
      USD to true -> R.string.currency_name_usd_buy
      USD to false -> R.string.currency_name_usd_sell
      EUR to true -> R.string.currency_name_eur_buy
      EUR to false -> R.string.currency_name_eur_sell
      RUB to true -> R.string.currency_name_rub_buy
      RUB to false -> R.string.currency_name_rub_sell
      RUR to true -> R.string.currency_name_rub_buy
      RUR to false -> R.string.currency_name_rub_sell
      else -> 0
    }

  private fun BestCourse.toBestCurrencyRate(@StringRes currencyNameRes: Int): BestCurrencyRate =
    BestCurrencyRate(id, bank, currencyNameRes, currencyValue)
}
