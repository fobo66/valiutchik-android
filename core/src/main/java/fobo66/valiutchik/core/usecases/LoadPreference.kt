package fobo66.valiutchik.core.usecases

import kotlinx.coroutines.flow.Flow

interface LoadPreference<T> {
  fun execute(key: String): Flow<T>
}
