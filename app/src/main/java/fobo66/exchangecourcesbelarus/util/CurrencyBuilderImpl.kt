package fobo66.exchangecourcesbelarus.util

import com.google.common.base.CaseFormat.LOWER_CAMEL
import com.google.common.base.CaseFormat.LOWER_UNDERSCORE
import fobo66.exchangecourcesbelarus.models.Currency

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */
class CurrencyBuilderImpl : CurrencyBuilder {
  private val currency: Currency

  @Throws(NoSuchFieldException::class, IllegalAccessException::class)
  override fun with(fieldName: String, fieldValue: String): CurrencyBuilder {
    val field = Currency::class.java.getDeclaredField(convertFieldName(fieldName))
    field.isAccessible = true
    field[currency] = fieldValue
    return this
  }

  private fun convertFieldName(fieldName: String): String {
    return LOWER_UNDERSCORE.to(LOWER_CAMEL, fieldName)
  }

  override fun build(): Currency {
    return currency
  }

  init {
    currency = Currency()
  }
}