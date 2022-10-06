package fobo66.valiutchik.core.model.repository

import android.content.Intent
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSource
import javax.inject.Inject
import timber.log.Timber

class MapRepositoryImpl @Inject constructor(
  private val uriDataSource: UriDataSource,
  private val intentDataSource: IntentDataSource
) : MapRepository {
  override fun searchOnMap(query: CharSequence): Intent? {
    val mapUri = uriDataSource.prepareUri(
      URI_SCHEME,
      URI_AUTHORITY,
      URI_PARAM_KEY,
      query.toString()
    )
    val intent = intentDataSource.createIntent(mapUri)

    val canResolveIntent = intentDataSource.resolveIntent(intent) != null

    return if (canResolveIntent) {
      intent
    } else {
      Timber.e("Cannot show banks on map: maps app not found")
      null
    }
  }

  companion object {
    const val URI_SCHEME = "geo"
    const val URI_AUTHORITY = "0,0"
    const val URI_PARAM_KEY = "q"
  }
}
