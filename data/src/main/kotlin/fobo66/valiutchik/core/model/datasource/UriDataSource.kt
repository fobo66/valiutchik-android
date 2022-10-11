package fobo66.valiutchik.core.model.datasource

import android.net.Uri

interface UriDataSource {
  /**
   * Forge an URI for the intent with single query parameter
   */
  fun prepareUri(
    scheme: String,
    authority: String,
    queryParameterKey: String,
    queryParameterValue: String
  ): Uri
}