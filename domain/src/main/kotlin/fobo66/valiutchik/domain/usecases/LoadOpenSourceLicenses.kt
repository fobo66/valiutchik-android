package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.domain.entities.OpenSourceLicense
import kotlinx.coroutines.flow.Flow

interface LoadOpenSourceLicenses {
  fun execute(): Flow<List<OpenSourceLicense>>
}
