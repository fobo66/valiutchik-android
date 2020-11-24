package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.valiutchik.core.BUY_COURSE
import fobo66.valiutchik.core.EUR
import fobo66.valiutchik.core.RUR
import fobo66.valiutchik.core.SELL_COURSE
import fobo66.valiutchik.core.UNKNOWN_COURSE
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
class BestCourseProducer @Inject constructor() {

  private val bankNamePattern: Pattern by lazy { "([\"«])[^\"]*([\"»])".toPattern() }

  private val currencyKeys by lazy { listOf(USD, EUR, RUR) }

  fun findBestBuyCourses(
    courses: Set<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()

    currencyKeys.forEach { currencyKey ->
      val currency = courses.asSequence()
        .filter { isBuyRateCorrect(it, currencyKey) }
        .maxByOrNull { it.resolveCurrencyBuyRate(currencyKey) } ?: courses.first {
        isBuyRateCorrect(it, currencyKey)
      }
      result.add(
        BestCourse(
          0L,
          escapeBankName(currency.bankname),
          currency.resolveCurrencyBuyRate(currencyKey),
          currencyKey,
          timestamp,
          BUY_COURSE
        )
      )
    }
    return result
  }

  fun findBestSellCourses(
    courses: Set<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()

    currencyKeys.forEach { currencyKey ->
      val currency = courses.asSequence()
        .filter { isSellRateCorrect(it, currencyKey) }
        .minByOrNull { it.resolveCurrencySellRate(currencyKey) } ?: courses.first {
        isSellRateCorrect(it, currencyKey)
      }
      result.add(
        BestCourse(
          0L,
          escapeBankName(currency.bankname),
          currency.resolveCurrencySellRate(currencyKey),
          currencyKey,
          timestamp,
          SELL_COURSE
        )
      )
    }
    return result
  }

  private fun isSellRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencySellRate(currencyKey) != UNKNOWN_COURSE

  private fun isBuyRateCorrect(
    currency: Currency,
    currencyKey: String
  ) = currency.resolveCurrencyBuyRate(currencyKey) != UNKNOWN_COURSE

  private fun escapeBankName(bankName: String): String {
    val matcher = bankNamePattern.matcher(bankName)
    return if (matcher.find()) matcher.group(0) ?: bankName else bankName
  }
}