package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.valiutchik.core.entities.Location

class FakeLocationDataSource : LocationDataSource {
  override suspend fun resolveLocation(): Location =
    Location(0.0, 0.0)
}
