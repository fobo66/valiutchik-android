package fobo66.valiutchik.core.fake

import fobo66.valiutchik.core.model.datasource.BestCourseDataSource

class FakeBestCourseDataSource : BestCourseDataSource {
  override fun findBestBuyCurrencies(courses: Set<fobo66.valiutchik.api.Currency>): Map<String, fobo66.valiutchik.api.Currency> =
    emptyMap()

  override fun findBestSellCurrencies(courses: Set<fobo66.valiutchik.api.Currency>): Map<String, fobo66.valiutchik.api.Currency> =
    emptyMap()
}
