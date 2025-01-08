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

import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

/**
 * Currency model from XML
 */
@Serializable
@XmlSerialName("bank")
data class Bank(
  @XmlElement
  @XmlSerialName("bankid")
  val bankId: String = "",
  @XmlElement
  @XmlSerialName("filialid")
  val filialId: String = "",
  @XmlElement
  @XmlSerialName("date")
  val date: String = "",
  @XmlElement
  @XmlSerialName("bankname")
  val bankName: String = "",
  @XmlElement
  @XmlSerialName("filialname")
  val filialName: String = "",
  @XmlElement
  @XmlSerialName("bankaddress")
  val bankAddress: String = "",
  @XmlElement
  @XmlSerialName("bankphone")
  val bankPhone: String = "",
  @XmlElement
  @XmlSerialName("usd_buy")
  val usdBuy: String = "",
  @XmlElement
  @XmlSerialName("usd_sell")
  val usdSell: String = "",
  @XmlElement
  @XmlSerialName("eur_buy")
  val eurBuy: String = "",
  @XmlElement
  @XmlSerialName("eur_sell")
  val eurSell: String = "",
  @XmlElement
  @XmlSerialName("rub_buy")
  val rubBuy: String = "",
  @XmlElement
  @XmlSerialName("rub_sell")
  val rubSell: String = "",
  @XmlElement
  @XmlSerialName("pln_buy")
  val plnBuy: String = "",
  @XmlElement
  @XmlSerialName("pln_sell")
  val plnSell: String = "",
  @XmlElement
  @XmlSerialName("uah_buy")
  val uahBuy: String = "",
  @XmlElement
  @XmlSerialName("uah_sell")
  val uahSell: String = "",
  @XmlElement
  @XmlSerialName("eurusd_sell")
  val conversionSell: String = "",
  @XmlElement
  @XmlSerialName("eurusd_buy")
  val conversionBuy: String = "",
)
