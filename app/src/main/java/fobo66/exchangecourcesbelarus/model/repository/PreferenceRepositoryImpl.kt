package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import fobo66.valiutchik.core.KEY_DEFAULT_CITY
import fobo66.valiutchik.core.KEY_UPDATE_INTERVAL
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceRepositoryImpl @Inject constructor(
  private val preferencesDataSource: PreferencesDataSource
) : PreferenceRepository {
  override fun observeDefaultCityPreference(): Flow<String> =
    preferencesDataSource.observeString(KEY_DEFAULT_CITY, DEFAULT_CITY)

  override fun observeUpdateIntervalPreference(): Flow<Float> =
    preferencesDataSource.observeInt(KEY_UPDATE_INTERVAL, DEFAULT_UPDATE_INTERVAL)
      .map { it.toFloat() }

  override suspend fun updateDefaultCityPreference(newValue: String) {
    preferencesDataSource.saveString(KEY_DEFAULT_CITY, newValue)
  }

  override suspend fun updateUpdateIntervalPreference(newValue: Float) {
    preferencesDataSource.saveInt(KEY_UPDATE_INTERVAL, newValue.toInt())
  }

  companion object {
    private const val DEFAULT_UPDATE_INTERVAL = 3
    private const val DEFAULT_CITY = "Минск"
  }
}
