package fobo66.valiutchik.core.util

import fobo66.valiutchik.core.entities.Currency
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test

class CurrencyListSanitizerImplTest {

  private lateinit var sanitizer: CurrencyListSanitizer

  @Before
  fun setUp() {
    sanitizer = CurrencyListSanitizerImpl()
  }

  @Test
  fun `default currency with empty fields is invalid`() {
    val result = sanitizer.sanitize(
      setOf(Currency())
    )

    assertTrue(result.isEmpty())
  }

  @Test
  fun `currency with one empty field is valid`() {
    val result = sanitizer.sanitize(
      setOf(Currency(
        "test",
        usdBuy = "1",
        usdSell = "1",
        eurBuy = "1",
        eurSell = "1",
        rurBuy = "1",
      ))
    )

    assertEquals(1, result.size)
  }

  @Test
  fun `currency with one unknown field is valid`() {
    val result = sanitizer.sanitize(
      setOf(Currency(
        "test",
        usdBuy = "1",
        usdSell = "1",
        eurBuy = "1",
        eurSell = "-",
        rurBuy = "1",
        rurSell = "1"
      ))
    )

    assertEquals(1, result.size)
  }

  @Test
  fun `currency with all unknown field is invalid`() {
    val result = sanitizer.sanitize(
      setOf(Currency(
        "test",
        usdBuy = "-",
        usdSell = "-",
        eurBuy = "-",
        eurSell = "-",
        rurBuy = "-",
        rurSell = "-"
      ))
    )

    assertTrue(result.isEmpty())
  }
}