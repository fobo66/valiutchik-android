package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.repository.PreferenceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePreferenceRepository : PreferenceRepository {
  var defaultCity = "default"
  var updateInterval = 3.0f

  override fun observeDefaultCityPreference(): Flow<String> =
    flowOf(defaultCity)

  override fun observeUpdateIntervalPreference(): Flow<Float> =
    flowOf(updateInterval)

  override suspend fun updateDefaultCityPreference(newValue: String) = Unit

  override suspend fun updateUpdateIntervalPreference(newValue: Float) = Unit
}
