package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.LicensesRepository
import fobo66.valiutchik.domain.entities.OpenSourceLicense
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoadOpenSourceLicensesImpl @Inject constructor(
  private val licensesRepository: LicensesRepository
) : LoadOpenSourceLicenses {
  override fun execute(): Flow<List<OpenSourceLicense>> = flow {
    val licenses = licensesRepository.loadLicenses()
      .map {
        OpenSourceLicense(
          developers = it.developers,
          licenses = it.licenses.map { license -> license.license },
          project = it.project,
          url = it.url,
          version = it.version,
          year = it.year
        )
      }
    emit(licenses)
  }
}
