package fobo66.exchangecourcesbelarus.model.datasource

import android.net.Uri
import javax.inject.Inject

class UriDataSource @Inject constructor() {
  /**
   * Forge an URI for the intent with single query parameter
   */
  fun prepareUri(
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
