package fobo66.valiutchik.core.util

import fobo66.valiutchik.core.TAG_NAME_BANKNAME
import fobo66.valiutchik.core.TAG_NAME_EUR_BUY
import fobo66.valiutchik.core.TAG_NAME_EUR_SELL
import fobo66.valiutchik.core.TAG_NAME_RUR_BUY
import fobo66.valiutchik.core.TAG_NAME_RUR_SELL
import fobo66.valiutchik.core.TAG_NAME_USD_BUY
import fobo66.valiutchik.core.TAG_NAME_USD_SELL
import fobo66.valiutchik.core.entities.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */
class CurrencyBuilderImpl : CurrencyBuilder {
  private val currencyMap = mutableMapOf<String, String>()

  override fun with(fieldName: String, fieldValue: String): CurrencyBuilder {
    currencyMap[fieldName] = fieldValue
    return this
  }

  override fun build(): Currency {
    return Currency(
      currencyMap[TAG_NAME_BANKNAME].orEmpty(),
      currencyMap[TAG_NAME_USD_BUY].orEmpty(),
      currencyMap[TAG_NAME_USD_SELL].orEmpty(),
      currencyMap[TAG_NAME_EUR_BUY].orEmpty(),
      currencyMap[TAG_NAME_EUR_SELL].orEmpty(),
      currencyMap[TAG_NAME_RUR_BUY].orEmpty(),
      currencyMap[TAG_NAME_RUR_SELL].orEmpty()
    )
  }
}