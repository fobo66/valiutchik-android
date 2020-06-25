package fobo66.exchangecourcesbelarus.model.datasource

import okhttp3.Response

interface CurrencyRatesDataSource {
  suspend fun loadExchangeRates(city: String): Response
}