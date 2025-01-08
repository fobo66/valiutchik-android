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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.entity.Banks
import fobo66.valiutchik.api.entity.Currency
import nl.adaptivity.xmlutil.serialization.XML
import java.io.InputStream

class CurrencyRatesParserSerializerImpl : CurrencyRatesParser {
  override fun parse(inputStream: InputStream): Set<Currency> {
    val banks = XML.decodeFromString<Banks>(inputStream.readBytes().toString(Charsets.UTF_8))
    return banks.banks
      .map {
        Currency(
          bankname = it.bankName,
          usdBuy = it.usdBuy,
          usdSell = it.usdSell,
          eurBuy = it.eurBuy,
          eurSell = it.eurSell,
          rurBuy = it.rubBuy,
          rurSell = it.rubSell,
          plnBuy = it.plnBuy,
          plnSell = it.plnSell,
          uahBuy = it.uahBuy,
          uahSell = it.uahSell,
        )
      }.toSet()
  }
}
