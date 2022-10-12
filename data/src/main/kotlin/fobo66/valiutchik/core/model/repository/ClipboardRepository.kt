package fobo66.valiutchik.core.model.repository

interface ClipboardRepository {
  fun copyToClipboard(label: CharSequence, value: CharSequence)
}
