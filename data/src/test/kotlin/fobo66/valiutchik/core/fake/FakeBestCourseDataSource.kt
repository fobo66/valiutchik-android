package fobo66.valiutchik.core.fake

import fobo66.valiutchik.api.Currency
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource

class FakeBestCourseDataSource : BestCourseDataSource {
  override fun findBestBuyCurrencies(courses: Set<Currency>): Map<String, Currency> =
    emptyMap()

  override fun findBestSellCurrencies(courses: Set<Currency>): Map<String, Currency> =
    emptyMap()
}
