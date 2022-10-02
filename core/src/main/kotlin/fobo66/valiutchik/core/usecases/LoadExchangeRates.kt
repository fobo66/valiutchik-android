package fobo66.valiutchik.core.usecases

import fobo66.valiutchik.core.entities.BestCurrencyRate
import kotlinx.coroutines.flow.Flow

interface LoadExchangeRates {
  fun execute(): Flow<List<BestCurrencyRate>>
}
