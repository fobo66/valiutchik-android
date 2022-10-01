package fobo66.exchangecourcesbelarus.entities

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
data class LicensesState(
  val licenses: ImmutableList<LicenseItem>
)
