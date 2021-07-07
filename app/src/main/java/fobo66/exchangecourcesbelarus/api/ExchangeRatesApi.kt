package fobo66.exchangecourcesbelarus.api

import fobo66.valiutchik.core.entities.Currency
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRatesApi {

  @GET("/outer/authXml/{city}")
  suspend fun loadExchangeRates(@Path("city") cityIndex: String): Set<Currency>
}
