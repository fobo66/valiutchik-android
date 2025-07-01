/*
 *    Copyright 2025 Andrey Mukamolov
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

package fobo66.valiutchik.core.entities

import com.google.common.truth.Truth.assertThat
import fobo66.valiutchik.api.entity.Bank
import fobo66.valiutchik.api.entity.ExchangeRateValue
import fobo66.valiutchik.api.entity.UNDEFINED_BUY_RATE
import fobo66.valiutchik.api.entity.UNDEFINED_SELL_RATE
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.char
import org.junit.jupiter.api.Test

private const val ID = 1L
private const val TEST = "test"
private val RATE = ExchangeRateValue(1.23)
private val UNKNOWN_COURSE = ExchangeRateValue(-1.0)
private const val DATE = "2025-07-01"
private const val RAW_DATE = "01.07.2025"

class MappersTest {
    @Test
    fun `ids are concatenated`() {
        val rate = buildBank().toRate(LocalDate.Formats.ISO)
        assertThat(rate.id).isEqualTo(11L)
    }

    @Test
    fun `ids have different digit counts`() {
        val rate = buildBank(
            bankId = 12L,
            filialId = 3L
        ).toRate(LocalDate.Formats.ISO)
        assertThat(rate.id).isEqualTo(123L)
    }

    @Test
    fun `primary id is zero`() {
        val rate = buildBank(
            bankId = 0L
        ).toRate(LocalDate.Formats.ISO)
        assertThat(rate.id).isEqualTo(ID)
    }

    @Test
    fun `secondary id is zero`() {
        val rate = buildBank(
            bankId = 12L,
            filialId = 0L
        ).toRate(LocalDate.Formats.ISO)
        assertThat(rate.id).isEqualTo(120L)
    }

    @Test
    fun `date is parsed by format`() {
        val rate = buildBank(
            date = RAW_DATE
        ).toRate(
            LocalDate.Format {
                day()
                char('.')
                monthNumber()
                char('.')
                year()
            }
        )
        assertThat(rate.date).isEqualTo(DATE)
    }

    @Test
    fun `unknown buy rate`() {
        val rate = buildBank(
            usdBuy = UNKNOWN_COURSE
        ).toRate(LocalDate.Formats.ISO)
        assertThat(rate.usdBuy).isEqualTo(UNDEFINED_BUY_RATE)
    }

    @Test
    fun `unknown sell rate`() {
        val rate = buildBank(
            usdSell = UNKNOWN_COURSE
        ).toRate(LocalDate.Formats.ISO)
        assertThat(rate.usdSell).isEqualTo(UNDEFINED_SELL_RATE)
    }

    @Suppress("LongParameterList") // ok for tests
    private fun buildBank(
        bankId: Long = ID,
        filialId: Long = ID,
        date: String = DATE,
        bankName: String = TEST,
        filialName: String = TEST,
        bankAddress: String = TEST,
        bankPhone: String = TEST,
        usdBuy: ExchangeRateValue = RATE,
        usdSell: ExchangeRateValue = RATE,
        eurBuy: ExchangeRateValue = RATE,
        eurSell: ExchangeRateValue = RATE,
        rubBuy: ExchangeRateValue = RATE,
        rubSell: ExchangeRateValue = RATE,
        plnBuy: ExchangeRateValue = RATE,
        plnSell: ExchangeRateValue = RATE,
        uahBuy: ExchangeRateValue = RATE,
        uahSell: ExchangeRateValue = RATE
    ): Bank = Bank(
        bankId,
        filialId,
        date,
        bankName,
        filialName,
        bankAddress,
        bankPhone,
        usdBuy,
        usdSell,
        eurBuy,
        eurSell,
        rubBuy,
        rubSell,
        plnBuy,
        plnSell,
        uahBuy,
        uahSell
    )
}
