package fobo66.valiutchik.domain.entities

data class OpenSourceLicense(
  val developers: List<String>,
  val licenses: List<String>,
  val project: String,
  val url: String?,
  val version: String,
  val year: String?
)
