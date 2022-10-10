package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.PreferenceRepository
import javax.inject.Inject
import timber.log.Timber

class UpdateUpdateIntervalPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : UpdateUpdateIntervalPreference {
  override suspend fun execute(newUpdateInterval: Float) {
    Timber.v("Saving new update interval: %s", newUpdateInterval)
    preferenceRepository.updateUpdateIntervalPreference(newUpdateInterval)
    Timber.v("Saved!")
  }
}
