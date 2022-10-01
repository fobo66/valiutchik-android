package fobo66.valiutchik.core.entities

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class OpenSourceLicenses(
  @Json(name = "licenses")
  val licenses: List<OpenSourceLicensesItem>
)
