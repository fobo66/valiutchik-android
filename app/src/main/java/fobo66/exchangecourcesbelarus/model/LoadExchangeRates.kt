package fobo66.exchangecourcesbelarus.model

import fobo66.valiutchik.core.entities.BestCurrencyRate
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LoadExchangeRates {
  fun execute(isBuy: Boolean): Flow<List<BestCurrencyRate>>
}

class LoadExchangeRatesImpl @Inject constructor(
  private val currencyRateRepository: CurrencyRateRepository
) : LoadExchangeRates {
  override fun execute(isBuy: Boolean): Flow<List<BestCurrencyRate>> {
    return currencyRateRepository.loadExchangeRates(isBuy)
      .map {
        it.map { bestCourse -> bestCourse.toBestCurrencyRate() }
      }
  }
}