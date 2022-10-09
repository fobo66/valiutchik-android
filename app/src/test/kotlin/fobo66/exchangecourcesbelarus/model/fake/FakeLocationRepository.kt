package fobo66.exchangecourcesbelarus.model.fake

import fobo66.valiutchik.core.model.repository.LocationRepository

/**
 * Fake location repo implementation for tests
 */
class FakeLocationRepository : LocationRepository {
  var isResolved = false
  override suspend fun resolveUserCity(defaultCity: String): String {
    isResolved = true
    return "fake"
  }
}
