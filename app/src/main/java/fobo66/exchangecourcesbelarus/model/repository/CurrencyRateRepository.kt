package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.di.Io
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.CurrencyRatesParser
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import fobo66.exchangecourcesbelarus.util.TIMESTAMP_NEW
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime
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

  suspend fun loadExchangeRates(city: String): List<BestCourse> {
    val now = LocalDateTime.now()
    val timestamp = loadTimestamp(now)

    if (needToUpdateCurrencyRates(Duration.between(timestamp, now))) {
      val currenciesResponse = try {
        currencyRatesDataSource.loadExchangeRates(city)
      } catch (e: Exception) {
        null
      }

      currenciesResponse?.body?.let {
        withContext(ioDispatcher) {
          val nowString = now.toString()
          preferencesDataSource.saveString(TIMESTAMP_NEW, nowString)
          val currencies = parser.parse(it.byteStream())

          val bestBuyCourses = currencyEvaluator.findBestBuyCourses(currencies, nowString)
          val bestSellCourses = currencyEvaluator.findBestSellCourses(currencies, nowString)

          val bestCourses =
            mutableListOf(*bestBuyCourses.toTypedArray(), *bestSellCourses.toTypedArray())

          persistenceDataSource.saveBestCourses(bestCourses)
        }
      }
    }

    return persistenceDataSource.loadBestCourses(timestamp.toString())
  }

  private fun loadTimestamp(fallbackTimestamp: LocalDateTime): LocalDateTime {
    val rawTimestamp: String = preferencesDataSource.loadSting(TIMESTAMP_NEW)

    return if (rawTimestamp.isEmpty()) {
      fallbackTimestamp
    } else {
      LocalDateTime.parse(rawTimestamp)
    }
  }

  private fun needToUpdateCurrencyRates(cachedValueAge: Duration): Boolean =
    cachedValueAge == Duration.ZERO || cachedValueAge > maxStalePeriod

}