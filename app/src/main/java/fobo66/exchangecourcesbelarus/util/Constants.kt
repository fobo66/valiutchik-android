package fobo66.exchangecourcesbelarus.util

import androidx.annotation.StringDef
import kotlin.annotation.AnnotationRetention.SOURCE

/**
 * Constants used in project
 * Created by fobo66 on 09.08.2015.
 */
const val GEOCODER_ACCESS_TOKEN =
  "pk.eyJ1IjoiZm9ibzY2IiwiYSI6ImNqZHJvZ2k1OTBsZjMzM3BkNG0zanoyMGMifQ.g825XQ7bhGHwSW0cggrJcQ"
const val TEMPLATE_URI = "https://admin.myfin.by/outer/authXml/%d"
const val BASE_URL = "https://admin.myfin.by/outer/authXml"
const val USD = "USD"
const val EUR = "EUR"
const val RUR = "RUR"
const val BUY_COURSE = true
const val SELL_COURSE = false
const val LOCATION_PERMISSION_REQUEST = 746
const val INTERNET_PERMISSIONS_REQUEST = 66
const val BROADCAST_ACTION_SUCCESS =
  "fobo66.exchangecourcesbelarus.model.CurrencyRateService.SUCCESS"
const val BROADCAST_ACTION_ERROR =
  "fobo66.exchangecourcesbelarus.model.CurrencyRateService.ERROR"
const val ACTION_FETCH_COURSES = "fobo66.exchangecourcesbelarus.action.FETCH_COURSES"
const val EXTRA_CITY = "fobo66.exchangecourcesbelarus.extra.CITY"
const val EXTRA_BESTCOURSES = "fobo66.exchangecourcesbelarus.extra.BESTCOURSES"
const val EXTRA_BUYORSELL = "fobo66.exchangecourcesbelarus.extra.BUYORSELL"
const val TIMESTAMP = "timestamp"
const val TIMESTAMP_NEW = "fobo66.exchangecourcesbelarus.TIMESTAMP"
const val USER_CITY_KEY = "fobo66.exchangecourcesbelarus.USER_CITY"

@Retention(SOURCE)
@StringDef(value = [USD, EUR, RUR])
annotation class CurrencyName
