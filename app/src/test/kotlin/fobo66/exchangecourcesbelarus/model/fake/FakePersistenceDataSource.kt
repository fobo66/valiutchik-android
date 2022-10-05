package fobo66.exchangecourcesbelarus.model.fake

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.datasource.PersistenceDataSource
import fobo66.valiutchik.core.util.Resettable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakePersistenceDataSource : PersistenceDataSource, Resettable {
  var isSaved = false

  override suspend fun saveBestCourses(bestCourses: List<BestCourse>) {
    isSaved = true
  }

  override fun readBestCourses(): Flow<List<BestCourse>> = emptyFlow()

  override fun reset() {
    isSaved = false
  }
}
