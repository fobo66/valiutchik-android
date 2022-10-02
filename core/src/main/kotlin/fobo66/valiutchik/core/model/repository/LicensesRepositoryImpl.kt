package fobo66.valiutchik.core.model.repository

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import fobo66.valiutchik.core.entities.OpenSourceLicenses
import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import javax.inject.Inject

class LicensesRepositoryImpl @Inject constructor(
  private val assetsDataSource: AssetsDataSource
) : LicensesRepository {

  @OptIn(ExperimentalStdlibApi::class)
  private val jsonAdapter: JsonAdapter<OpenSourceLicenses> by lazy {
    Moshi.Builder().build().adapter()
  }

  override fun loadLicenses(): List<OpenSourceLicensesItem> {
    val licensesFile = assetsDataSource.loadFile("open_source_licenses.json")
    return jsonAdapter.fromJson(licensesFile)?.licenses ?: emptyList()
  }
}
