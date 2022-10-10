package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.entities.BestCourse
import kotlinx.coroutines.flow.Flow

interface PersistenceDataSource {
  suspend fun saveBestCourses(bestCourses: List<BestCourse>)
  fun readBestCourses(): Flow<List<BestCourse>>
}
