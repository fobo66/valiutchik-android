package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.TIMESTAMP
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class CurrencyRatesTimestampRepositoryTest {

  private lateinit var currencyRatesTimestampRepository: CurrencyRatesTimestampRepository

  private val preferencesDataSource = mockk<PreferencesDataSource> {
    every {
      loadInt(any(), any())
    } returns 3
  }

  private val now = LocalDateTime.now()

  @Before
  fun setUp() {
    currencyRatesTimestampRepository = CurrencyRatesTimestampRepository(preferencesDataSource)
  }

  @Test
  fun `no timestamp - need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns ""

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `now timestamp - need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp is old - need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusDays(1).toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp slightly in the past - no need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(1).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp on the limit - no need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(3).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp above customized limit - need to update`() {
    every {
      preferencesDataSource.loadInt(any(), any())
    } returns 2

    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.minusHours(3).toString()

    assertTrue(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }

  @Test
  fun `timestamp in the future - no need to update`() {
    every {
      preferencesDataSource.loadString(TIMESTAMP)
    } returns now.plusHours(1).toString()

    assertFalse(currencyRatesTimestampRepository.isNeededToUpdateCurrencyRates(now))
  }
}
