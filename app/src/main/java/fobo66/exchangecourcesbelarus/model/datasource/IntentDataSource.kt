package fobo66.exchangecourcesbelarus.model.datasource

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import fobo66.exchangecourcesbelarus.R
import javax.inject.Inject

class IntentDataSource @Inject constructor(
  @ApplicationContext private val context: Context
) {
  fun createIntent(uri: Uri, action: String = Intent.ACTION_VIEW): Intent = Intent(action, uri)

  fun resolveIntent(intent: Intent): ComponentName? =
    intent.resolveActivity(context.packageManager)
}
