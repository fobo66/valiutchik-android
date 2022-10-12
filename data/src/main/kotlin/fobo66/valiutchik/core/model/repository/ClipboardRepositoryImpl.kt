package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import javax.inject.Inject
import timber.log.Timber

class ClipboardRepositoryImpl @Inject constructor(
  private val clipboardDataSource: ClipboardDataSource
) : ClipboardRepository {

  override fun copyToClipboard(label: CharSequence, value: CharSequence) {
    Timber.v("Copying to clipboard: label = %s, value = %s", label, value)
    if (clipboardDataSource.copyToClipboard(label, value)) {
      Timber.v("Copied successfully")
    } else {
      Timber.v("Copying failed")
    }
  }
}
