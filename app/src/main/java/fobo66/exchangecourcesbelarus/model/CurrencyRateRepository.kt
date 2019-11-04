package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepository(
  private val parser: CurrencyCourseParser,
  private val currencyEvaluator: CurrencyEvaluator,
  private val myfinDataSource: MyfinDataSource,
  private val cacheDataSource: CacheDataSource
) {
}