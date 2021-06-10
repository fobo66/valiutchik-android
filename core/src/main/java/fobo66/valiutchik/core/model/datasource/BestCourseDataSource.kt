package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUB
import fobo66.valiutchik.core.UNKNOWN_COURSE
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.resolveCurrencyBuyRate
import fobo66.valiutchik.core.util.resolveCurrencySellRate
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Algorithm for finding best currency rate for each currency
 * Created by fobo66 on 05.02.2017.
 */
@Singleton
class BestCourseDataSource @Inject constructor() {

  private val currencyKeys by lazy { listOf(USD, EUR, RUB) }

  fun findBestBuyCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency> = currencyKeys.associateWith { currencyKey ->
    courses.asSequence()
      .filter { isBuyRateCorrect(it, currencyKey) }
      .maxByOrNull { it.resolveCurrencyBuyRate(currencyKey) } ?: courses.first {
      isBuyRateCorrect(it, currencyKey)
    }
  }

  fun findBestSellCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency> = currencyKeys.associateWith { currencyKey ->
    courses.asSequence()
      .filter { isSellRateCorrect(it, currencyKey) }
      .minByOrNull { it.resolveCurrencySellRate(currencyKey) } ?: courses.first {
      isSellRateCorrect(it, currencyKey)
    }
  }

  private fun isSellRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencySellRate(currencyKey) != UNKNOWN_COURSE

  private fun isBuyRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencyBuyRate(currencyKey) != UNKNOWN_COURSE
}
