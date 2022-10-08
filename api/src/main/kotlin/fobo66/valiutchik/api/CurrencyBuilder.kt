package fobo66.valiutchik.api

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */
interface CurrencyBuilder {
  fun with(fieldName: String, fieldValue: String): CurrencyBuilder

  fun build(): Currency
}
