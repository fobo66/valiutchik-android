package fobo66.exchangecourcesbelarus.model.datasource

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IntentDataSource @Inject constructor(
  @ApplicationContext private val context: Context
) {
  fun createIntent(uri: Uri, action: String = Intent.ACTION_VIEW): Intent = Intent(action, uri)

  fun resolveIntent(intent: Intent): ComponentName? =
    intent.resolveActivity(context.packageManager)
}
