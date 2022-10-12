package fobo66.valiutchik.core.model.datasource

import android.content.ComponentName
import android.content.Intent
import android.net.Uri

interface IntentDataSource {
  fun createIntent(uri: Uri, action: String = Intent.ACTION_VIEW): Intent
  fun resolveIntent(intent: Intent): ComponentName?
}
