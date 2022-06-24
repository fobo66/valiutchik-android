package fobo66.exchangecourcesbelarus.model.repository

import kotlinx.coroutines.flow.Flow

interface PreferenceRepository {
  fun observeDefaultCityPreference(): Flow<String>
  fun observeUpdateIntervalPreference(): Flow<Float>
  suspend fun updateDefaultCityPreference(newValue: String)
  suspend fun updateUpdateIntervalPreference(newValue: Float)
}
