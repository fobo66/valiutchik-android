package fobo66.valiutchik.core.usecases

interface CopyCurrencyRateToClipboard {
  fun execute(currencyName: CharSequence, currencyValue: CharSequence)
}
