package fobo66.exchangecourcesbelarus.model.fake

import fobo66.valiutchik.core.model.datasource.CurrencyRatesDataSource
import fobo66.valiutchik.core.util.Resettable
import java.io.IOException

class FakeCurrencyRatesDataSource : CurrencyRatesDataSource, Resettable {
  var isError = false
  override suspend fun loadExchangeRates(city: String): Set<fobo66.valiutchik.api.Currency> =
    if (isError) {
      throw IOException("test")
    } else {
      setOf(fobo66.valiutchik.api.Currency())
    }

  override fun reset() {
    isError = false
  }
}
