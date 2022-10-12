package fobo66.valiutchik.core.fake

import fobo66.valiutchik.core.entities.Location
import fobo66.valiutchik.core.model.datasource.LocationDataSource

class FakeLocationDataSource : LocationDataSource {
  override suspend fun resolveLocation(): Location =
    Location(0.0, 0.0)
}
