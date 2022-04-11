package fobo66.exchangecourcesbelarus.model.fake

import fobo66.valiutchik.core.model.repository.LocationRepository

/**
 * Fake location repo implementation for tests
 */
class FakeLocationRepository : LocationRepository, Resettable {
  var isResolved = false
  override suspend fun resolveUserCity(): String {
    isResolved = true
    return "fake"
  }

  override fun reset() {
    isResolved = false
  }
}
