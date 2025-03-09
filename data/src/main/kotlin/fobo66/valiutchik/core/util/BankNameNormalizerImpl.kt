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

package fobo66.valiutchik.core.util

import kotlin.LazyThreadSafetyMode.NONE

private const val QUOTE = '\"'
private const val OPEN_TYPOGRAPHIC_QUOTE = '«'
private const val CLOSING_TYPOGRAPHIC_QUOTE = '»'

class BankNameNormalizerImpl : BankNameNormalizer {
  private val quotes by lazy(NONE) {
    charArrayOf(QUOTE, OPEN_TYPOGRAPHIC_QUOTE, CLOSING_TYPOGRAPHIC_QUOTE)
  }

  override fun normalize(bankName: String): String {
    val startTypographicalQuotePosition = bankName.indexOfFirst { it == OPEN_TYPOGRAPHIC_QUOTE }
    val startQuotePosition = bankName.indexOfFirst { it == QUOTE }

    if (startQuotePosition == -1 && startTypographicalQuotePosition == -1) {
      return bankName
    }

    val canonicalBankName =
      if (startTypographicalQuotePosition == -1 ||
        (startQuotePosition in 1 until startTypographicalQuotePosition)
      ) {
        val endQuotePosition = bankName.indexOf(QUOTE, startQuotePosition + 1)
        bankName.substring(startQuotePosition + 1, endQuotePosition)
      } else {
        val endQuotePosition = bankName.indexOfFirst { it == CLOSING_TYPOGRAPHIC_QUOTE }
        bankName.substring(startTypographicalQuotePosition + 1, endQuotePosition)
      }

    return canonicalBankName.filterNot { quotes.contains(it) }
  }
}
