package fobo66.exchangecourcesbelarus.util

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CurrencyBuilderTest {

  private lateinit var currencyBuilder: CurrencyBuilder

  @Before
  fun setUp() {
    currencyBuilder = CurrencyBuilderImpl()
  }

  @Test
  fun fieldNameCorrect() {
    val currency = currencyBuilder.with("bankname", "test").build()
    assertEquals("test", currency.bankname)
  }

  @Test
  fun fieldNameIncorrect_empty() {
    val currency = currencyBuilder.with("test", "test").build()
    assertEquals("", currency.bankname)
  }

  @Test
  fun fieldNameCorrect_rewrite() {
    val currency = currencyBuilder
      .with("bankname", "test")
      .with("bankname", "test2")
      .build()
    assertEquals("test2", currency.bankname)
  }
}
