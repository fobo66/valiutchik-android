package fobo66.exchangecourcesbelarus.entities

data class PreferenceRequest<T>(
  val key: String,
  val defaultValue: T,
)
