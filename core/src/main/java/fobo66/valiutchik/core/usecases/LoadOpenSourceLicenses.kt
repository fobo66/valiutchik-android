package fobo66.valiutchik.core.usecases

import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import kotlinx.coroutines.flow.Flow

interface LoadOpenSourceLicenses {
  fun execute(): Flow<List<OpenSourceLicensesItem>>
}
