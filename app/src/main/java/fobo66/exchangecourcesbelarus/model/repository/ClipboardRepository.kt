package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.ClipboardDataSource
import timber.log.Timber
import javax.inject.Inject

class ClipboardRepository @Inject constructor(
  private val clipboardDataSource: ClipboardDataSource
) {

  fun copyToClipboard(label: CharSequence, value: CharSequence) {
    Timber.d("Copying to clipboard: $1%s -> $2%s", label, value)
    if (clipboardDataSource.copyToClipboard(label, value)) {
      Timber.d("Copied successfully")
    } else {
      Timber.d("Copying failed")
    }
  }
}
