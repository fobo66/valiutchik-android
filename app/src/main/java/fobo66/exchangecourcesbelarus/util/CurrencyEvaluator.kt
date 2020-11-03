package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.CurrencyName
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.USD
import fobo66.valiutchik.core.entities.Currency
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 05.02.2017.
 */
@Singleton
class CurrencyEvaluator @Inject constructor() {

  private val pattern: Pattern by lazy { "([\"«])[^\"]*([\"»])".toPattern() }

  private val currencyKeys by lazy { listOf(USD, EUR, RUR) }

  fun findBestBuyCourses(
    courses: List<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()

    currencyKeys.forEach { currencyKey ->
      val currency =
        courses.maxByOrNull { resolveCurrencyBuyValue(it, currencyKey) } ?: courses.first()
      result.add(
        BestCourse(
          0L,
          escapeBankName(currency.bankname),
          resolveCurrencyBuyValue(currency, currencyKey),
          currencyKey,
          timestamp,
          BUY_COURSE
        )
      )
    }
    return result
  }

  fun findBestSellCourses(
    courses: List<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()

    currencyKeys.forEach { currencyKey ->
      val currency =
        courses.minByOrNull { resolveCurrencySellValue(it, currencyKey) } ?: courses.first()
      result.add(
        BestCourse(
          0L,
          escapeBankName(currency.bankname),
          resolveCurrencySellValue(currency, currencyKey),
          currencyKey,
          timestamp,
          SELL_COURSE
        )
      )
    }
    return result
  }

  private fun escapeBankName(bankName: String): String {
    val matcher = pattern.matcher(bankName)
    return if (matcher.find()) matcher.group(0) ?: bankName else bankName
  }

  /**
   * Method to figure out which currency will be used depends on the context
   * By default, USD value is returned
   * If I find the better way to do it, I'll rewrite it.
   */
  private fun resolveCurrencyBuyValue(currency: Currency, @CurrencyName name: String): String {
    return when (name) {
      EUR -> currency.eurBuy
      RUR -> currency.rurBuy
      USD -> currency.usdBuy
      else -> currency.usdBuy
    }
  }

  /**
   * See above.
   */
  private fun resolveCurrencySellValue(currency: Currency, @CurrencyName name: String): String {
    return when (name) {
      EUR -> currency.eurSell
      RUR -> currency.rurSell
      USD -> currency.usdSell
      else -> currency.usdSell
    }
  }
}