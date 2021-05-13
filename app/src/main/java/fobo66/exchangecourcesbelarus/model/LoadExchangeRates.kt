package fobo66.exchangecourcesbelarus.model

import fobo66.exchangecourcesbelarus.entities.BestCourse
import fobo66.exchangecourcesbelarus.model.repository.CurrencyRateRepository
import fobo66.valiutchik.core.entities.BestCurrencyRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface LoadExchangeRates {
  fun execute(isBuy: Boolean): Flow<List<BestCurrencyRate>>
}

class LoadExchangeRatesImpl @Inject constructor(
  private val currencyRateRepository: CurrencyRateRepository
) : LoadExchangeRates {
  override fun execute(isBuy: Boolean): Flow<List<BestCurrencyRate>> =
    currencyRateRepository.loadExchangeRates(isBuy)
      .map {
        val buyRates: List<BestCourse> = it.filter { it.isBuy }
        val sellRates: List<BestCourse> = it.filter { !it.isBuy }

        buyRates.zip(sellRates) { buyRate, sellRate ->
          BestCurrencyRate(
            buyRate.id,
            buyRate.bank,
            sellRate.bank,
            buyRate.currencyName,
            buyRate.currencyValue,
            sellRate.currencyValue
          )
        }
      }
}
