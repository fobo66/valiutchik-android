package fobo66.valiutchik.core.usecases

import kotlinx.coroutines.flow.Flow

interface LoadUpdateIntervalPreference {
  fun execute(): Flow<Float>
}
