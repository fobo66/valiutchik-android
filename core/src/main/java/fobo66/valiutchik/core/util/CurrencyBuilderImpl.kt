package fobo66.valiutchik.core.util

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
      currencyMap["bankname"].orEmpty(),
      currencyMap["usd_buy"].orEmpty(),
      currencyMap["usd_sell"].orEmpty(),
      currencyMap["eur_buy"].orEmpty(),
      currencyMap["eur_sell"].orEmpty(),
      currencyMap["rur_buy"].orEmpty(),
      currencyMap["rur_sell"].orEmpty()
    )
  }
}