package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.domain.entities.BestCurrencyRate
import kotlinx.coroutines.flow.Flow

interface LoadExchangeRates {
  fun execute(): Flow<List<BestCurrencyRate>>
}
