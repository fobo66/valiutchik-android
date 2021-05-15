package fobo66.valiutchik.core.usecases

import java.time.LocalDateTime

interface RefreshExchangeRates {
  suspend fun execute(latitude: Double, longitude: Double, now: LocalDateTime)
}
