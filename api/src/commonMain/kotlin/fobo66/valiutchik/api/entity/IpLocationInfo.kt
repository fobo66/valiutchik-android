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
data class IpLocationInfo(
    @SerialName("city")
    val city: String,
    @SerialName("continent_code")
    val continentCode: String,
    @SerialName("continent_name")
    val continentName: String,
    @SerialName("country_capital")
    val countryCapital: String,
    @SerialName("country_code2")
    val countryCode2: String,
    @SerialName("country_code3")
    val countryCode3: String,
    @SerialName("country_emoji")
    val countryEmoji: String,
    @SerialName("country_flag")
    val countryFlag: String,
    @SerialName("country_name")
    val countryName: String,
    @SerialName("country_name_official")
    val countryNameOfficial: String,
    @SerialName("district")
    val district: String,
    @SerialName("geoname_id")
    val geonameId: String,
    @SerialName("is_eu")
    val isEu: Boolean,
    @SerialName("latitude")
    val latitude: String,
    @SerialName("longitude")
    val longitude: String,
    @SerialName("state_code")
    val stateCode: String,
    @SerialName("state_prov")
    val stateProv: String,
    @SerialName("zipcode")
    val zipcode: String
)
