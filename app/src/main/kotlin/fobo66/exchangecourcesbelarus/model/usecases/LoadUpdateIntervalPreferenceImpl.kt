package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.exchangecourcesbelarus.model.repository.PreferenceRepository
import fobo66.valiutchik.domain.usecases.LoadUpdateIntervalPreference
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoadUpdateIntervalPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : LoadUpdateIntervalPreference {
  override fun execute(): Flow<Float> {
    return preferenceRepository.observeUpdateIntervalPreference()
  }
}
