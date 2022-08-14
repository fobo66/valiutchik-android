package fobo66.exchangecourcesbelarus.model.datasource

import android.net.Uri
import javax.inject.Inject

class UriDataSourceImpl @Inject constructor() : UriDataSource {
  /**
   * Forge an URI for the intent with single query parameter
   */
  override fun prepareUri(
    scheme: String,
    authority: String,
    queryParameterKey: String,
    queryParameterValue: String
  ): Uri = Uri.Builder()
    .scheme(scheme)
    .authority(authority)
    .appendQueryParameter(queryParameterKey, queryParameterValue)
    .build()
}
