package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.PreferenceRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoadDefaultCityPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : LoadDefaultCityPreference {
  override fun execute(): Flow<String> = preferenceRepository.observeDefaultCityPreference()
}
