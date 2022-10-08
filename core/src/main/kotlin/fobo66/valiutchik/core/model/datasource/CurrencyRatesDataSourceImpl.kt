package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.api.ExchangeRatesApi
import javax.inject.Inject

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/4/19.
 */
class CurrencyRatesDataSourceImpl @Inject constructor(
  private val exchangeRatesApi: ExchangeRatesApi
) : CurrencyRatesDataSource {

  private val citiesMap: Map<String, String> by lazy {
    mapOf(
      "Минск" to "1",
      "Витебск" to "2",
      "Гомель" to "3",
      "Гродно" to "4",
      "Брест" to "5",
      "Могилёв" to "6"
    )
  }

  override suspend fun loadExchangeRates(city: String): Set<Currency> {
    val cityIndex = citiesMap[city] ?: "1"

    return exchangeRatesApi.loadExchangeRates(cityIndex)
  }
}
