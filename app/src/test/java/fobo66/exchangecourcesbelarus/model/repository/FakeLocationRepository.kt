package fobo66.exchangecourcesbelarus.model.repository

import fobo66.valiutchik.core.model.repository.LocationRepository

/**
 * Fake location repo implementation for tests
 */
class FakeLocationRepository : LocationRepository {
  var isResolved = false
  override suspend fun resolveUserCity(): String {
    isResolved = true
    return "fake"
  }
}
