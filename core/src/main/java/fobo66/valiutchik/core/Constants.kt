package fobo66.valiutchik.core

import androidx.annotation.StringDef
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * Constants used in project
 * Created by fobo66 on 09.08.2015.
 */
const val GEOCODER_ACCESS_TOKEN =
  "pk.eyJ1IjoiZm9ibzY2IiwiYSI6ImNqZHJvZ2k1OTBsZjMzM3BkNG0zanoyMGMifQ.g825XQ7bhGHwSW0cggrJcQ"
const val BASE_URL = "https://admin.myfin.by/outer/authXml"
const val USD = "USD"
const val EUR = "EUR"
const val RUB = "RUB"
const val RUR = "RUR"
const val BUY_COURSE = true
const val SELL_COURSE = false

const val TIMESTAMP = "fobo66.exchangecourcesbelarus.TIMESTAMP"
const val USER_CITY_KEY = "fobo66.exchangecourcesbelarus.USER_CITY"

const val TAG_NAME_BANKNAME = "bankname"
const val TAG_NAME_USD_BUY = "usd_buy"
const val TAG_NAME_USD_SELL = "usd_sell"
const val TAG_NAME_EUR_BUY = "eur_buy"
const val TAG_NAME_EUR_SELL = "eur_sell"
const val TAG_NAME_RUR_BUY = "rub_buy"
const val TAG_NAME_RUR_SELL = "rub_sell"

const val UNKNOWN_COURSE = "-"

@Retention(SOURCE)
@StringDef(value = [USD, EUR, RUB, RUR])
annotation class CurrencyName