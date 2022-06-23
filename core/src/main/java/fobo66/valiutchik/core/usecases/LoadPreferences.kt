package fobo66.valiutchik.core.usecases

import fobo66.valiutchik.core.entities.PreferenceRequest
import kotlinx.coroutines.flow.Flow

interface LoadPreferences {
  fun execute(): Flow<List<PreferenceRequest<out Any>>>
}
