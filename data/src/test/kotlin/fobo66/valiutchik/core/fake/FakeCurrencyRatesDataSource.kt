package fobo66.valiutchik.core.fake

import fobo66.valiutchik.core.model.datasource.CurrencyRatesDataSource
import java.io.IOException

class FakeCurrencyRatesDataSource : CurrencyRatesDataSource {
  var isError = false
  override suspend fun loadExchangeRates(city: String): Set<fobo66.valiutchik.api.Currency> =
    if (isError) {
      throw IOException("test")
    } else {
      setOf(fobo66.valiutchik.api.Currency())
    }
}
