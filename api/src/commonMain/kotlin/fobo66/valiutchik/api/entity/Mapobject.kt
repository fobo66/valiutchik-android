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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Mapobject(
    @SerialName("adress")
    val address: String,
    @SerialName("bank_alias")
    val bankAlias: String,
    @SerialName("bank_icon")
    val bankIcon: String,
    @SerialName("bank_id")
    val bankId: Long,
    @SerialName("bank_logo")
    val bankLogo: String,
    @SerialName("bank_name")
    val bankName: String,
    @SerialName("bank_phone")
    val bankPhone: String,
    @SerialName("city_id")
    val cityId: String,
    @SerialName("currency")
    val currency: Currency,
    @SerialName("distance")
    val distance: String,
    @SerialName("geo")
    val geo: Geo,
    @SerialName("id")
    val id: Long,
    @SerialName("sef_alias")
    val sefAlias: String,
    @SerialName("time_to_distance")
    val timeToDistance: Int,
    @SerialName("working_time")
    val workingTime: List<List<String>>
)
