package fobo66.exchangecourcesbelarus.model.datasource

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClipboardDataSource @Inject constructor(
  @ApplicationContext private val context: Context
) {

  fun copyToClipboard(label: CharSequence, value: CharSequence): Boolean {
    val clipData = ClipData.newPlainText(label, value)
    val clipboardManager = context.getSystemService<ClipboardManager>()
    return clipboardManager?.let {
      it.setPrimaryClip(clipData)
      true
    } ?: false
  }
}
