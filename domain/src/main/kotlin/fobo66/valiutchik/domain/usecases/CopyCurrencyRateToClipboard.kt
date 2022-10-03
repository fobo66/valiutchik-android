package fobo66.valiutchik.domain.usecases

interface CopyCurrencyRateToClipboard {
  fun execute(currencyName: CharSequence, currencyValue: CharSequence)
}
