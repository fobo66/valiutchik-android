/*
 *    Copyright 2024 Andrey Mukamolov
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
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_PLN_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_PLN_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_RUR_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_RUR_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_UAH_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_UAH_SELL
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_USD_BUY
import fobo66.valiutchik.api.CurrencyRatesParserImpl.Companion.TAG_NAME_USD_SELL

/**
 * Builder for currency object
 */
fun MutableMap<String, String>.toCurrency(): Currency =
  Currency(
    bankname = get(TAG_NAME_BANKNAME).orEmpty(),
    usdBuy = get(TAG_NAME_USD_BUY).orEmpty(),
    usdSell = get(TAG_NAME_USD_SELL).orEmpty(),
    eurBuy = get(TAG_NAME_EUR_BUY).orEmpty(),
    eurSell = get(TAG_NAME_EUR_SELL).orEmpty(),
    rurBuy = get(TAG_NAME_RUR_BUY).orEmpty(),
    rurSell = get(TAG_NAME_RUR_SELL).orEmpty(),
    plnBuy = get(TAG_NAME_PLN_BUY).orEmpty(),
    plnSell = get(TAG_NAME_PLN_SELL).orEmpty(),
    uahBuy = get(TAG_NAME_UAH_BUY).orEmpty(),
    uahSell = get(TAG_NAME_UAH_SELL).orEmpty()
  )
