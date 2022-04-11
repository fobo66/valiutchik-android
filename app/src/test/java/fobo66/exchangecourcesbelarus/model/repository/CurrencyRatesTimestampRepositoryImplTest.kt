package fobo66.exchangecourcesbelarus.model.repository

import java.time.LocalDateTime
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CurrencyRatesTimestampRepositoryImplTest {

  private lateinit var currencyRatesTimestampRepository: CurrencyRatesTimestampRepository

  private val preferencesDataSource = FakePreferenceDataSource()

  private val now = LocalDateTime.now()

  @Before
  fun setUp() {
    currencyRatesTimestampRepository = CurrencyRatesTimestampRepositoryImpl(preferencesDataSource)
  }

  @After
  fun tearDown() {
    preferencesDataSource.reset()
  }

  @Test
  fun `no timestamp - need to update`() {
    preferencesDataSource.string = ""

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `now timestamp - need to update`() {
    preferencesDataSource.string = now.toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp is old - need to update`() {
    preferencesDataSource.string = now.minusDays(1).toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    preferencesDataSource.string = now.minusHours(1).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    preferencesDataSource.string = now.minusHours(3).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    preferencesDataSource.int = 2
    preferencesDataSource.string = now.minusHours(3).toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    preferencesDataSource.string = now.plusHours(1).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }
}
