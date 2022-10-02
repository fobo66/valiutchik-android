package fobo66.exchangecourcesbelarus.model.fake

import fobo66.valiutchik.core.entities.Currency
import fobo66.valiutchik.core.model.datasource.BestCourseDataSource

class FakeBestCourseDataSource : BestCourseDataSource {
  override fun findBestBuyCurrencies(courses: Set<Currency>): Map<String, Currency> = emptyMap()

  override fun findBestSellCurrencies(courses: Set<Currency>): Map<String, Currency> = emptyMap()
}
