package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.di.Io
import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.BestCourseProducer
import fobo66.exchangecourcesbelarus.util.resolveCurrencyBuyRate
import fobo66.exchangecourcesbelarus.util.resolveCurrencySellRate
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.CurrencyRatesParser
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.LocalDateTime
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepository @Inject constructor(
  private val parser: CurrencyRatesParser,
  private val bestCourseProducer: BestCourseProducer,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  @Io private val ioDispatcher: CoroutineDispatcher
) {
  private val bankNamePattern: Pattern by lazy { "([\"«])[^\"]*([\"»])".toPattern() }

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
    return resolveBuyRates(currencies, now) + resolveSellRates(currencies, now)
  }

  private fun resolveBuyRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseProducer.findBestBuyCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        escapeBankName(currency.bankname),
        currency.resolveCurrencyBuyRate(currencyKey),
        currencyKey,
        now,
        BUY_COURSE
      )
    }

  private fun resolveSellRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseProducer.findBestSellCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        escapeBankName(currency.bankname),
        currency.resolveCurrencySellRate(currencyKey),
        currencyKey,
        now,
        SELL_COURSE
      )
    }

  private fun escapeBankName(bankName: String): String {
    val matcher = bankNamePattern.matcher(bankName)
    return if (matcher.find()) matcher.group(0) ?: bankName else bankName
  }
}