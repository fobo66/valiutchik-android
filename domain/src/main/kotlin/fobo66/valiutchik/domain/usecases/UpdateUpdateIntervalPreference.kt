package fobo66.valiutchik.domain.usecases

interface UpdateUpdateIntervalPreference {
  suspend fun execute(newUpdateInterval: Float)
}
