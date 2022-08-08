package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.PreferenceRepository
import fobo66.valiutchik.core.entities.PreferenceRequest
import fobo66.valiutchik.core.usecases.LoadPreferences
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class LoadPreferencesImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : LoadPreferences {
  override fun execute(): Flow<List<PreferenceRequest<out Any>>> {
    return combine(
      preferenceRepository.observeDefaultCityPreference(),
      preferenceRepository.observeUpdateIntervalPreference()
    ) { defaultCity, updateInterval ->
      listOf()
    }
  }
}
