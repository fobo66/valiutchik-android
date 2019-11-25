package fobo66.exchangecourcesbelarus.util

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.entities.Currency
import fobo66.exchangecourcesbelarus.util.comparators.CurrencyComparator
import fobo66.exchangecourcesbelarus.util.comparators.EurBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.EurSellComparator
import fobo66.exchangecourcesbelarus.util.comparators.RurBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.RurSellComparator
import fobo66.exchangecourcesbelarus.util.comparators.UsdBuyComparator
import fobo66.exchangecourcesbelarus.util.comparators.UsdSellComparator
import java.util.Collections
import java.util.regex.Pattern
import javax.inject.Inject

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 05.02.2017.
 */
class CurrencyEvaluator @Inject constructor(sanitizer: CurrencyListSanitizer) {
  private val pattern: Pattern
  private val sanitizer: CurrencyListSanitizer
  private var comparatorsMap: MutableMap<String, CurrencyComparator> = mutableMapOf()

  fun findBestBuyCourses(tempSet: Set<Currency>): List<BestCourse> {
    val result: MutableList<BestCourse> = ArrayList()
    var currency: Currency
    var workList: MutableList<Currency> = ArrayList(tempSet)
    initializeBuyComparators()
    workList = sanitizer.sanitize(workList)
    for (currencyKey in comparatorsMap.keys) {
      Collections.sort(workList, comparatorsMap[currencyKey])
      currency = workList[0]
      result.add(
        BestCourse(
          escapeBankName(currency.bankname),
          resolveCurrencyBuyValue(currency, currencyKey), currencyKey, BUY_COURSE
        )
      )
    }
    return result
  }

  fun findBestSellCourses(tempSet: Set<Currency>): List<BestCourse> {
    var currency: Currency
    val result: MutableList<BestCourse> = ArrayList()
    initializeSellComparators()
    var workList: MutableList<Currency> = ArrayList(tempSet)
    workList = sanitizer.sanitize(workList)
    for (currencyKey in comparatorsMap.keys) {
      Collections.sort(workList, Collections.reverseOrder(comparatorsMap[currencyKey]))
      currency = workList[0]
      result.add(
        BestCourse(
          escapeBankName(currency.bankname),
          resolveCurrencySellValue(currency, currencyKey), currencyKey, SELL_COURSE
        )
      )
    }
    return result
  }

  private fun initializeBuyComparators() {
    comparatorsMap = mutableMapOf(
      USD to UsdBuyComparator(),
      EUR to EurBuyComparator(),
      RUR to RurBuyComparator()
    )
  }

  private fun initializeSellComparators() {
    comparatorsMap = mutableMapOf(
      USD to UsdSellComparator(),
      EUR to EurSellComparator(),
      RUR to RurSellComparator()
    )
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

  companion object {
    private const val regexpForEscapingBankName = "([\"«])[^\"]*([\"»])"
  }

  init {
    pattern = Pattern.compile(regexpForEscapingBankName)
    this.sanitizer = sanitizer
  }
}