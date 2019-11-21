package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.util.CurrencyEvaluator
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRateRepository @Inject constructor(
  private val parser: CurrencyRatesParser,
  private val currencyEvaluator: CurrencyEvaluator,
  private val myfinDataSource: MyfinDataSource,
  private val persistenceDataSource: PersistenceDataSource
)