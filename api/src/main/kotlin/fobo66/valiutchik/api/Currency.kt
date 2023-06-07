/*
 *    Copyright 2023 Andrey Mukamolov
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

/**
 * Currency model from XML
 *
 * Created by fobo66 on 16.08.2015.
 */
data class Currency(
  val bankname: String = "",
  val usdBuy: String = "",
  val usdSell: String = "",
  val eurBuy: String = "",
  val eurSell: String = "",
  val rurBuy: String = "",
  val rurSell: String = "",
  val plnBuy: String = "",
  val plnSell: String = "",
  val uahBuy: String = "",
  val uahSell: String = ""
)
