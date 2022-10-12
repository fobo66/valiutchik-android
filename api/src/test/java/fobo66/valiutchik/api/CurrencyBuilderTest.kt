/*
 *    Copyright 2022 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.api

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CurrencyBuilderTest {

  private val currencyBuilder: CurrencyBuilder = CurrencyBuilderImpl()

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
