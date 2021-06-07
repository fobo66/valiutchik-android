package fobo66.exchangecourcesbelarus.model.repository

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClipboardRepository @Inject constructor(
  @ApplicationContext private val context: Context
) {

  fun copyToClipboard(label: CharSequence, value: CharSequence) {
    val clipData = ClipData.newPlainText(label, value)
    val clipboardManager = context.getSystemService<ClipboardManager>()
    clipboardManager?.setPrimaryClip(clipData)
  }
}
