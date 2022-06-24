package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PreferenceRepositoryImpl @Inject constructor(
  private val preferencesDataSource: PreferencesDataSource
): PreferenceRepository {
  override fun observeDefaultCityPreference(): Flow<String> {
    TODO("Not yet implemented")
  }

  override fun observeUpdateIntervalPreference(): Flow<Float> {
    TODO("Not yet implemented")
  }

  override suspend fun updateDefaultCityPreference(newValue: String) {
    TODO("Not yet implemented")
  }

  override suspend fun updateUpdateIntervalPreference(newValue: Float) {
    TODO("Not yet implemented")
  }
}
