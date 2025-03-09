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

package fobo66.valiutchik.core.model.datasource

import android.icu.number.NumberFormatter
import android.icu.util.Currency
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import fobo66.valiutchik.core.util.BankNameNormalizer
import java.util.Locale

private const val BYN = "BYN"

class FormattingDataSourceImpl(
  private val locale: Locale,
  private val bankNameNormalizer: BankNameNormalizer,
) : FormattingDataSource {
  override fun formatBankName(name: String): String = bankNameNormalizer.normalize(name)

  override fun formatCurrencyValue(value: Double): String =
    if (VERSION.SDK_INT >= VERSION_CODES.R) {
      NumberFormatter
        .withLocale(locale)
        .unit(Currency.getInstance(BYN))
        .format(value)
        .toString()
    } else {
      value.toString()
    }
}
