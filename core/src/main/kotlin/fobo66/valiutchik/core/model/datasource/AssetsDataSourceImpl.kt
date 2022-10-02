package fobo66.valiutchik.core.model.datasource

import android.content.res.AssetManager
import javax.inject.Inject
import okio.BufferedSource
import okio.buffer
import okio.source

class AssetsDataSourceImpl @Inject constructor(
  private val assetManager: AssetManager
) : AssetsDataSource {
  override fun loadFile(fileName: String): BufferedSource {
    return assetManager.open(fileName).source().buffer()
  }
}
