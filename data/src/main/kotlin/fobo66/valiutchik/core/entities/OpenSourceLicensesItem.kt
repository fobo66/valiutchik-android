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

package fobo66.valiutchik.core.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenSourceLicensesItem(
  @Json(name = "dependency")
  val dependency: String,
  @Json(name = "description")
  val description: String?,
  @Json(name = "developers")
  val developers: List<String>,
  @Json(name = "licenses")
  val licenses: List<License>,
  @Json(name = "project")
  val project: String,
  @Json(name = "url")
  val url: String?,
  @Json(name = "version")
  val version: String,
  @Json(name = "year")
  val year: String?
)
