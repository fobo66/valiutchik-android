package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.exchangecourcesbelarus.util.BankNameNormalizer
import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.api.resolveCurrencyBuyRate
import fobo66.valiutchik.api.resolveCurrencySellRate
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.di.Io
import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource
import fobo66.valiutchik.core.model.datasource.CurrencyRatesDataSource
import java.io.IOException
import java.time.LocalDateTime
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepositoryImpl @Inject constructor(
  private val bestCourseDataSource: BestCourseDataSource,
  private val persistenceDataSource: PersistenceDataSource,
  private val currencyRatesDataSource: CurrencyRatesDataSource,
  private val bankNameNormalizer: BankNameNormalizer,
  @Io private val ioDispatcher: CoroutineDispatcher
) : CurrencyRateRepository {

  override suspend fun refreshExchangeRates(city: String, now: LocalDateTime) {
    val currencies = try {
      currencyRatesDataSource.loadExchangeRates(city)
    } catch (e: IOException) {
      throw CurrencyRatesLoadFailedException(e)
    }

    val bestCourses: List<BestCourse>
    withContext(ioDispatcher) {
      bestCourses = findBestCourses(currencies, now.toString())
    }
    persistenceDataSource.saveBestCourses(bestCourses)
  }

  override fun loadExchangeRates(): Flow<List<BestCourse>> =
    persistenceDataSource.readBestCourses()

  private fun findBestCourses(
    currencies: Set<Currency>,
    now: String
  ): List<BestCourse> {
    return resolveBuyRates(currencies, now) + resolveSellRates(currencies, now)
  }

  private fun resolveBuyRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseDataSource.findBestBuyCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankname),
        currency.resolveCurrencyBuyRate(currencyKey),
        currencyKey,
        now,
        BUY_COURSE
      )
    }

  private fun resolveSellRates(
    currencies: Set<Currency>,
    now: String
  ) = bestCourseDataSource.findBestSellCurrencies(currencies)
    .map { (currencyKey, currency) ->
      BestCourse(
        0L,
        bankNameNormalizer.normalize(currency.bankname),
        currency.resolveCurrencySellRate(currencyKey),
        currencyKey,
        now,
        SELL_COURSE
      )
    }
}
