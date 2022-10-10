package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.api.Currency

interface CurrencyRatesDataSource {
  suspend fun loadExchangeRates(city: String): Set<Currency>
}
