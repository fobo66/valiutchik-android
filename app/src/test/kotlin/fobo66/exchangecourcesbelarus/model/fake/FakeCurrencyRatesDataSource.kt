package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSource
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.util.Resettable
import java.io.IOException

class FakeCurrencyRatesDataSource : CurrencyRatesDataSource, Resettable {
  var isError = false
  override suspend fun loadExchangeRates(city: String): Set<Currency> =
    if (isError) {
      throw IOException("test")
    } else {
      setOf(Currency())
    }

  override fun reset() {
    isError = false
  }
}
