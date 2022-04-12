package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.valiutchik.core.entities.Currency
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class FakeCurrencyRatesDataSource : CurrencyRatesDataSource, Resettable {
  var isError = false
  override suspend fun loadExchangeRates(city: String): Set<Currency> =
    if (isError) {
      throw HttpException(Response.error<Set<Currency>>(500, "test".toResponseBody()))
    } else {
      setOf(Currency())
    }

  override fun reset() {
    isError = false
  }
}
