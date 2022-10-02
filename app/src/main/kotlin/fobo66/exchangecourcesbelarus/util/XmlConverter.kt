package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.CurrencyRatesParser
import okhttp3.ResponseBody
import retrofit2.Converter

class XmlConverter(private val parser: CurrencyRatesParser) : Converter<ResponseBody, Set<Currency>> {

  override fun convert(value: ResponseBody): Set<Currency> {
    return parser.parse(value.byteStream())
  }
}
