package fobo66.valiutchik.core.model.datasource

import android.content.Context
import javax.inject.Inject
import okio.BufferedSource
import okio.buffer
import okio.source

class AssetsDataSourceImpl @Inject constructor(
  private val context: Context
) : AssetsDataSource {
  override fun loadFile(fileName: String): BufferedSource {
    return context.assets.open(fileName).source().buffer()
  }
}
