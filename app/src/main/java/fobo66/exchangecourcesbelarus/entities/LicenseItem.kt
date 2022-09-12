package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Immutable

@Immutable
data class LicenseItem(
  val project: String,
  val licenses: String,
  val year: String,
  val authors: String,
  val url: String? = null
)
