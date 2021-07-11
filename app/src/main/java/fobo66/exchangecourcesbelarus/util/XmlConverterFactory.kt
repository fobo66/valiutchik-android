package fobo66.exchangecourcesbelarus.util

import fobo66.valiutchik.core.util.CurrencyRatesParser
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import javax.inject.Inject

class XmlConverterFactory @Inject constructor(
  private val parser: CurrencyRatesParser
) : Converter.Factory() {

  private val xmlConverter: XmlConverter by lazy {
      XmlConverter(parser)
  }

  override fun responseBodyConverter(
    type: Type,
    annotations: Array<out Annotation>,
    retrofit: Retrofit
  ): Converter<ResponseBody, *> = xmlConverter
}
