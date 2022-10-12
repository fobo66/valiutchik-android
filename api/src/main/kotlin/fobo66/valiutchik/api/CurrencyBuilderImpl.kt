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

import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_BANKNAME
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_EUR_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_EUR_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_RUR_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_RUR_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_USD_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_USD_SELL

/**
 * (c) 2017 Andrey Mukamolov aka fobo66
 * Created by fobo66 on 15.03.2017.
 */
class CurrencyBuilderImpl : CurrencyBuilder {
  private val currencyMap = mutableMapOf<String, String>()

  override fun with(fieldName: String, fieldValue: String): CurrencyBuilder {
    currencyMap[fieldName] = fieldValue
    return this
  }

  override fun build(): Currency {
    return Currency(
      currencyMap[TAG_NAME_BANKNAME].orEmpty(),
      currencyMap[TAG_NAME_USD_BUY].orEmpty(),
      currencyMap[TAG_NAME_USD_SELL].orEmpty(),
      currencyMap[TAG_NAME_EUR_BUY].orEmpty(),
      currencyMap[TAG_NAME_EUR_SELL].orEmpty(),
      currencyMap[TAG_NAME_RUR_BUY].orEmpty(),
      currencyMap[TAG_NAME_RUR_SELL].orEmpty()
    )
  }
}
