package fobo66.valiutchik.domain.usecases

import kotlinx.coroutines.flow.Flow

interface LoadUpdateIntervalPreference {
  fun execute(): Flow<Float>
}
