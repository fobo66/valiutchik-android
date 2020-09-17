package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.di.Io
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.TIMESTAMP
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Duration
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
  private val preferencesDataSource: PreferencesDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  @Io private val ioDispatcher: CoroutineDispatcher
) {

  private val maxStalePeriod: Duration = Duration.ofHours(3)

  suspend fun refreshExchangeRates(city: String, now: LocalDateTime): List<BestCourse> {
    val timestamp = loadTimestamp(now)

    if (needToUpdateCurrencyRates(Duration.between(timestamp, now))) {
      val currenciesResponse = try {
        currencyRatesDataSource.loadExchangeRates(city)
      } catch (e: Exception) {
        Timber.e(e, "Failed to load exchange rates")
        null
      }

      currenciesResponse?.body?.let {
        withContext(ioDispatcher) {
          val nowString = now.toString()
          preferencesDataSource.saveString(TIMESTAMP, nowString)
          val currencies = parser.parse(it.byteStream())

          val bestCourses = findBestCourses(currencies, nowString)

          persistenceDataSource.saveBestCourses(bestCourses)
        }
      }
    }

    return persistenceDataSource.loadBestCourses(timestamp.toString())
  }

  fun loadExchangeRates(isBuy: Boolean): Flow<List<BestCourse>> =
    persistenceDataSource.readBestCourses(isBuy)

  private fun findBestCourses(
    currencies: Set<Currency>,
    now: String
  ): List<BestCourse> {
    val bestCourses = mutableListOf<BestCourse>()
    bestCourses.addAll(currencyEvaluator.findBestBuyCourses(currencies, now))
    bestCourses.addAll(currencyEvaluator.findBestSellCourses(currencies, now))
    return bestCourses
  }

  private fun loadTimestamp(fallbackTimestamp: LocalDateTime): LocalDateTime {
    val rawTimestamp: String = preferencesDataSource.loadSting(TIMESTAMP)

    return if (rawTimestamp.isEmpty()) {
      fallbackTimestamp
    } else {
      LocalDateTime.parse(rawTimestamp)
    }
  }

  private fun needToUpdateCurrencyRates(cachedValueAge: Duration): Boolean =
    cachedValueAge == Duration.ZERO || cachedValueAge > maxStalePeriod
}