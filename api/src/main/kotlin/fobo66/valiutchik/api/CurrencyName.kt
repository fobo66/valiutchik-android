package fobo66.valiutchik.api

import androidx.annotation.StringDef

const val USD = "USD"
const val EUR = "EUR"
const val RUB = "RUB"
const val RUR = "RUR"

@Retention(AnnotationRetention.SOURCE)
@StringDef(value = [USD, EUR, RUB, RUR])
annotation class CurrencyName
