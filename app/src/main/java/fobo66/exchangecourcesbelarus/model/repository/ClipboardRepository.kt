package fobo66.exchangecourcesbelarus.model.repository

import fobo66.exchangecourcesbelarus.model.datasource.ClipboardDataSource
import timber.log.Timber
import javax.inject.Inject

class ClipboardRepository @Inject constructor(
  private val clipboardDataSource: ClipboardDataSource
) {

  fun copyToClipboard(label: CharSequence, value: CharSequence) {
    Timber.v("Copying to clipboard: label = %s, value = %s", label, value)
    if (clipboardDataSource.copyToClipboard(label, value)) {
      Timber.v("Copied successfully")
    } else {
      Timber.v("Copying failed")
    }
  }
}
