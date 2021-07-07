package fobo66.exchangecourcesbelarus.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface ExchangeRatesApi {

  @GET("/outer/authXml/{city}")
  suspend fun loadExchangeRates(@Path("city") cityIndex: String): ResponseBody
}
