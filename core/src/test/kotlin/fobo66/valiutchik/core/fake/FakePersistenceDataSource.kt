package fobo66.valiutchik.core.fake

import fobo66.valiutchik.core.entities.BestCourse
import fobo66.valiutchik.core.model.datasource.PersistenceDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakePersistenceDataSource : PersistenceDataSource {
  var isSaved = false

  override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    isSaved = true
  }

  override fun readBestCourses(): Flow<List<BestCourse>> = emptyFlow()
}
