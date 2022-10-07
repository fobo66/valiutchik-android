package fobo66.exchangecourcesbelarus.model.usecases

import fobo66.valiutchik.core.model.repository.PreferenceRepository
import fobo66.valiutchik.domain.usecases.LoadDefaultCityPreference
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class LoadDefaultCityPreferenceImpl @Inject constructor(
  private val preferenceRepository: PreferenceRepository
) : LoadDefaultCityPreference {
  override fun execute(): Flow<String> = preferenceRepository.observeDefaultCityPreference()
}
