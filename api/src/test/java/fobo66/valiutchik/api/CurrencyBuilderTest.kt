package fobo66.valiutchik.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CurrencyBuilderTest {

  private lateinit var currencyBuilder: fobo66.valiutchik.api.CurrencyBuilder

  @BeforeEach
  fun setUp() {
    currencyBuilder = fobo66.valiutchik.api.CurrencyBuilderImpl()
  }

  @Test
  fun `for correct field name value is present`() {
    val currency = currencyBuilder.with("bankname", "test").build()
    assertEquals("test", currency.bankname)
  }

  @Test
  fun `for incorrect field name value is empty`() {
    val currency = currencyBuilder.with("test", "test").build()
    assertEquals("", currency.bankname)
  }

  @Test
  fun `value is rewritten if specified multiple times`() {
    val currency = currencyBuilder
      .with("bankname", "test")
      .with("bankname", "test2")
      .build()
    assertEquals("test2", currency.bankname)
  }
}
