package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.util.comparators.EurBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.EurSellComparator
import fobo66.exchangecourcesbelarus.util.comparators.RurBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.RurSellComparator
import fobo66.exchangecourcesbelarus.util.comparators.UsdBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.UsdSellComparator
import java.util.Collections
import java.util.regex.Pattern
import javax.inject.Inject
import javax.inject.Singleton

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 05.02.2017.
 */
@Singleton
class CurrencyEvaluator @Inject constructor(private val sanitizer: CurrencyListSanitizer) {

  private val pattern: Pattern = "([\"«])[^\"]*([\"»])".toPattern()

  fun findBestBuyCourses(
    tempSet: Set<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()

    val workList: List<Currency> = tempSet.asSequence()
      .filter { !sanitizer.isInvalidEntry(it) }
      .toList()

    val comparatorsMap: Map<String, Comparator<Currency>> = initializeBuyComparators()

    comparatorsMap.keys.forEachIndexed { index, currencyKey ->
      Collections.sort(workList, comparatorsMap[currencyKey])
      val currency = workList.first()
      result.add(
        BestCourse(
          (index + 1).toLong(),
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
    tempSet: Set<Currency>,
    timestamp: String
  ): List<BestCourse> {
    val result: MutableList<BestCourse> = mutableListOf()
    val comparatorsMap = initializeSellComparators()
    val workList: List<Currency> = tempSet.asSequence()
      .filter { !sanitizer.isInvalidEntry(it) }
      .toList()

    comparatorsMap.keys.forEachIndexed { index, currencyKey ->
      Collections.sort(workList, Collections.reverseOrder(comparatorsMap[currencyKey]))
      val currency = workList.first()
      result.add(
        BestCourse(
          (index + 4).toLong(),
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

  private fun initializeBuyComparators() = mapOf(
      USD to UsdBuyComparator(),
      EUR to EurBuyComparator(),
      RUR to RurBuyComparator()
    )

  private fun initializeSellComparators() = mapOf(
      USD to UsdSellComparator(),
      EUR to EurSellComparator(),
      RUR to RurSellComparator()
    )

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
