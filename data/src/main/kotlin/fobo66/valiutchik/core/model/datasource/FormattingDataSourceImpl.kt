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

import android.content.Context
import android.icu.number.NumberFormatter
import android.icu.util.Currency
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.LocaleManagerCompat
import fobo66.valiutchik.core.util.BankNameNormalizer
import java.util.Locale
import kotlin.LazyThreadSafetyMode.NONE

private const val BYN = "BYN"

class FormattingDataSourceImpl(
  private val context: Context,
  private val bankNameNormalizer: BankNameNormalizer,
) : FormattingDataSource {
  private val locale: Locale by lazy(NONE) {
    val applicationLocales = LocaleManagerCompat.getApplicationLocales(context)
    val systemLocales = LocaleManagerCompat.getSystemLocales(context)
    val currentLocale =
      if (applicationLocales.isEmpty) {
        systemLocales.get(0)
      } else {
        applicationLocales.get(0)
      }
    currentLocale ?: Locale.getDefault()
  }

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
