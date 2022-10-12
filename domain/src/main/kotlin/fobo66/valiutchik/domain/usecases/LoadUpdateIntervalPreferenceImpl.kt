package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.PreferenceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoadUpdateIntervalPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : LoadUpdateIntervalPreference {
  override fun execute(): Flow<Float> {
    return preferenceRepository.observeUpdateIntervalPreference()
  }
}
