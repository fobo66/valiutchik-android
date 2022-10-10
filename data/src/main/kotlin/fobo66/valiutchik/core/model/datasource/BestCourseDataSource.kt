package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.api.Currency

interface BestCourseDataSource {
  fun findBestBuyCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency>

  fun findBestSellCurrencies(
    courses: Set<Currency>
  ): Map<String, Currency>
}
