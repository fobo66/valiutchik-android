package fobo66.valiutchik.core.usecases

import kotlinx.coroutines.flow.Flow

interface LoadDefaultCityPreference {
  fun execute(): Flow<String>
}
