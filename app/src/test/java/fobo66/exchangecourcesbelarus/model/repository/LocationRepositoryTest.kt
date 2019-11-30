package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.LocationDataSource
import fobo66.exchangecourcesbelarus.model.datasource.PreferencesDataSource
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

/**
 * (c) 2019 Andrey Mukamolov <fobo66@protonmail.com>
 * Created 12/1/19.
 */
class LocationRepositoryTest {

  lateinit var locationRepository: LocationRepository

  @Before
  fun setUp() {
    val locationDataSource: LocationDataSource = mockk()
    val preferencesDataSource: PreferencesDataSource = mockk()

    locationRepository = LocationRepository(locationDataSource, preferencesDataSource)
  }

  @Test
  fun resolveUserCity() {
  }
}