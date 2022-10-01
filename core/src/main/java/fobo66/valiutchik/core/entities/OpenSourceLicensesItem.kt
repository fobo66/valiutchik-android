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
