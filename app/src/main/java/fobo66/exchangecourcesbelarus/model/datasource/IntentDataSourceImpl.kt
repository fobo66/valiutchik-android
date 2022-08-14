package fobo66.exchangecourcesbelarus.model.datasource

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class IntentDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : IntentDataSource {
  override fun createIntent(uri: Uri, action: String): Intent = Intent(action, uri)

  override fun resolveIntent(intent: Intent): ComponentName? =
    intent.resolveActivity(context.packageManager)
}
