package fobo66.valiutchik.core.model.repository

import android.content.Intent

interface MapRepository {
  fun searchOnMap(query: CharSequence): Intent?
}
