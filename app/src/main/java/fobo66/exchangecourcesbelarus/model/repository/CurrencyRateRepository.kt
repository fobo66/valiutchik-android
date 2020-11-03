package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.di.Io
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.CurrencyListSanitizer
import fobo66.valiutchik.core.util.CurrencyRatesParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepository @Inject constructor(
  private val parser: CurrencyRatesParser,
  private val currencyEvaluator: CurrencyEvaluator,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  private val sanitizer: CurrencyListSanitizer,
  @Io private val ioDispatcher: CoroutineDispatcher
) {

  suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    val currenciesResponse = try {
      currencyRatesDataSource.loadExchangeRates(city)
    } catch (e: Exception) {
      Timber.e(e, "Failed to load exchange rates")
      null
    }

    currenciesResponse?.body?.let {
      withContext(ioDispatcher) {
        val currencies = parser.parse(it.byteStream())
        val bestCourses = findBestCourses(currencies, now.toString())

        persistenceDataSource.saveBestCourses(bestCourses)
      }
    }
  }

  fun loadExchangeRates(isBuy: Boolean): Flow<List<BestCourse>> =
    persistenceDataSource.readBestCourses(isBuy)

  private fun findBestCourses(
    currencies: Set<Currency>,
    now: String
  ): List<BestCourse> {
    val bestCourses = mutableListOf<BestCourse>()
    val clearCurrencies = sanitizeCurrencies(currencies)

    bestCourses.addAll(currencyEvaluator.findBestBuyCourses(clearCurrencies, now))
    bestCourses.addAll(currencyEvaluator.findBestSellCourses(clearCurrencies, now))
    return bestCourses
  }

  private fun sanitizeCurrencies(currencies: Set<Currency>): List<Currency> {
    return currencies.asSequence()
      .filter { !sanitizer.isInvalidEntry(it) }
      .toList()
  }
}
