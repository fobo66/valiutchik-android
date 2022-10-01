package fobo66.valiutchik.core.usecases

import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import fobo66.valiutchik.core.model.repository.LicensesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadOpenSourceLicensesImpl @Inject constructor(
  private val licensesRepository: LicensesRepository
) : LoadOpenSourceLicenses {
  override fun execute(): Flow<List<OpenSourceLicensesItem>> = flow {
    val licenses = licensesRepository.loadLicenses()
    emit(licenses)
  }
}
