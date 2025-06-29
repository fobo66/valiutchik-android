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

package fobo66.valiutchik.core.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenSourceLicensesItem(
    @SerialName("dependency")
    val dependency: String,
    @SerialName("description")
    val description: String?,
    @SerialName("developers")
    val developers: List<String>,
    @SerialName("licenses")
    val licenses: List<License>,
    @SerialName("project")
    val project: String,
    @SerialName("url")
    val url: String?,
    @SerialName("version")
    val version: String,
    @SerialName("year")
    val year: String?
)
