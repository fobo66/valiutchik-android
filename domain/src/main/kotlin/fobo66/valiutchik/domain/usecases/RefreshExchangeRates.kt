package fobo66.valiutchik.domain.usecases

import java.time.LocalDateTime

interface RefreshExchangeRates {
  suspend fun execute(now: LocalDateTime)
}
