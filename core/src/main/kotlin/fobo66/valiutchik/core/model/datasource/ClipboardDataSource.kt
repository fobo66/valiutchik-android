package fobo66.valiutchik.core.model.datasource

interface ClipboardDataSource {
  fun copyToClipboard(label: CharSequence, value: CharSequence): Boolean
}
