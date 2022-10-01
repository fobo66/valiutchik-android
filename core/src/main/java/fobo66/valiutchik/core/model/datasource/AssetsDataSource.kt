package fobo66.valiutchik.core.model.datasource

import okio.BufferedSource

interface AssetsDataSource {
  fun loadFile(fileName: String): BufferedSource
}
