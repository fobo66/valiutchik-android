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

package fobo66.valiutchik.api.entity

import fobo66.valiutchik.api.ENTRY_TAG_NAME
import fobo66.valiutchik.api.TAG_NAME_BANK_ADDRESS
import fobo66.valiutchik.api.TAG_NAME_BANK_ID
import fobo66.valiutchik.api.TAG_NAME_BANK_NAME
import fobo66.valiutchik.api.TAG_NAME_BANK_PHONE
import fobo66.valiutchik.api.TAG_NAME_DATE
import fobo66.valiutchik.api.TAG_NAME_EURUSD_BUY
import fobo66.valiutchik.api.TAG_NAME_EURUSD_SELL
import fobo66.valiutchik.api.TAG_NAME_EUR_BUY
import fobo66.valiutchik.api.TAG_NAME_EUR_SELL
import fobo66.valiutchik.api.TAG_NAME_FILIAL_ID
import fobo66.valiutchik.api.TAG_NAME_FILIAL_NAME
import fobo66.valiutchik.api.TAG_NAME_PLN_BUY
import fobo66.valiutchik.api.TAG_NAME_PLN_SELL
import fobo66.valiutchik.api.TAG_NAME_RUR_BUY
import fobo66.valiutchik.api.TAG_NAME_RUR_SELL
import fobo66.valiutchik.api.TAG_NAME_UAH_BUY
import fobo66.valiutchik.api.TAG_NAME_UAH_SELL
import fobo66.valiutchik.api.TAG_NAME_USD_BUY
import fobo66.valiutchik.api.TAG_NAME_USD_SELL
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Currency model from XML
 */
@Serializable
@XmlSerialName(ENTRY_TAG_NAME)
data class Bank(
    @XmlElement
    @XmlSerialName(TAG_NAME_BANK_ID)
    val bankId: Long = 0L,
    @XmlElement
    @XmlSerialName(TAG_NAME_FILIAL_ID)
    val filialId: Long = 0L,
    @XmlElement
    @XmlSerialName(TAG_NAME_DATE)
    val date: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_BANK_NAME)
    val bankName: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_FILIAL_NAME)
    val filialName: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_BANK_ADDRESS)
    val bankAddress: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_BANK_PHONE)
    val bankPhone: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_USD_BUY)
    val usdBuy: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_USD_SELL)
    val usdSell: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_EUR_BUY)
    val eurBuy: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_EUR_SELL)
    val eurSell: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_RUR_BUY)
    val rubBuy: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_RUR_SELL)
    val rubSell: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_PLN_BUY)
    val plnBuy: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_PLN_SELL)
    val plnSell: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_UAH_BUY)
    val uahBuy: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_UAH_SELL)
    val uahSell: ExchangeRateValue = ExchangeRateValue(0.0),
    @XmlElement
    @XmlSerialName(TAG_NAME_EURUSD_SELL)
    val conversionSell: String = "",
    @XmlElement
    @XmlSerialName(TAG_NAME_EURUSD_BUY)
    val conversionBuy: String = ""
)
