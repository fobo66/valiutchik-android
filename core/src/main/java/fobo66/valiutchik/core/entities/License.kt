package fobo66.valiutchik.core.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class License(
  @Json(name = "license")
  val license: String,
  @Json(name = "license_url")
  val licenseUrl: String
)
