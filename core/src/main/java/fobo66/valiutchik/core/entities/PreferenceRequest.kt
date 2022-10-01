package fobo66.valiutchik.core.entities

data class PreferenceRequest<T>(
  val key: String,
  val defaultValue: T,
)
