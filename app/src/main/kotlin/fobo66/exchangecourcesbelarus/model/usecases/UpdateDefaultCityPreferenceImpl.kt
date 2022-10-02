package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.PreferenceRepository
import fobo66.valiutchik.core.usecases.UpdateDefaultCityPreference
import javax.inject.Inject
import timber.log.Timber

class UpdateDefaultCityPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : UpdateDefaultCityPreference {
  override suspend fun execute(newDefaultCity: String) {
    Timber.v("Saving new default city: %s", newDefaultCity)
    preferenceRepository.updateDefaultCityPreference(newDefaultCity)
    Timber.v("Saved!")
  }
}
