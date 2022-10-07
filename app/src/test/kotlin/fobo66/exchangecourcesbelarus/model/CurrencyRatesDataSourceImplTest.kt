package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.api.ExchangeRatesApi
import fobo66.exchangecourcesbelarus.model.datasource.CurrencyRatesDataSourceImpl
import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.model.datasource.CurrencyRatesDataSource
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.mock.BehaviorDelegate
import retrofit2.mock.MockRetrofit
import retrofit2.mock.NetworkBehavior

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 11/7/19.
 */
@ExperimentalCoroutinesApi
class CurrencyRatesDataSourceImplTest {
  private lateinit var mockApi: BehaviorDelegate<ExchangeRatesApi>

  private lateinit var dataSource: CurrencyRatesDataSource

  @BeforeEach
  fun setUp() {
    val retrofit = Retrofit.Builder()
      .baseUrl("http://example.com")
      .build()

    val networkBehavior = NetworkBehavior.create()
    networkBehavior.setDelay(0, TimeUnit.MILLISECONDS)

    mockApi = MockRetrofit.Builder(retrofit)
      .networkBehavior(networkBehavior)
      .build().create(ExchangeRatesApi::class.java)
  }

  @Test
  fun `load exchange rates`() {
    dataSource = CurrencyRatesDataSourceImpl(
      mockApi.returningResponse(setOf<Currency>())
    )

    runTest {
      val response = dataSource.loadExchangeRates("Минск")
      assertNotNull(response)
    }
  }
}
