package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepository @Inject constructor(
  private val parser: CurrencyRatesParser,
  private val currencyEvaluator: CurrencyEvaluator,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource
) {

  fun loadExchangeRates(city: String): List<BestCurrencyRate> {
    return emptyList()
  }
}