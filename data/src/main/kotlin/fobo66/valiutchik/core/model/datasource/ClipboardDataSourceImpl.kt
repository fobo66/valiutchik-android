package fobo66.valiutchik.core.model.datasource

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClipboardDataSourceImpl @Inject constructor(
  @ApplicationContext private val context: Context
) : ClipboardDataSource {

  override fun copyToClipboard(label: CharSequence, value: CharSequence): Boolean {
    val clipData = ClipData.newPlainText(label, value)
    val clipboardManager = context.getSystemService<ClipboardManager>()
    return clipboardManager?.let {
      it.setPrimaryClip(clipData)
      true
    } ?: false
  }
}
