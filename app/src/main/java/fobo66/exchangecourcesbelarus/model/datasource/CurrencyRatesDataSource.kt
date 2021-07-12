package fobo66.exchangecourcesbelarus.model.datasource

import fobo66.valiutchik.core.entities.Currency

interface CurrencyRatesDataSource {
  suspend fun loadExchangeRates(city: String): Set<Currency>
}
