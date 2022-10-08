package fobo66.valiutchik.api

import okhttp3.ResponseBody
import retrofit2.Converter

class XmlConverter(private val parser: CurrencyRatesParser) :
  Converter<ResponseBody, Set<Currency>> {

  override fun convert(value: ResponseBody): Set<Currency> {
    return parser.parse(value.byteStream())
  }
}
