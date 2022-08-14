package fobo66.exchangecourcesbelarus.model.datasource

import fobo66.exchangecourcesbelarus.entities.BestCourse
import kotlinx.coroutines.flow.Flow

interface PersistenceDataSource {
  suspend fun saveBestCourses(bestCourses: List<BestCourse>)
  fun readBestCourses(): Flow<List<BestCourse>>
}
