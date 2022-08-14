package fobo66.valiutchik.core.usecases

interface UpdateUpdateIntervalPreference {
  suspend fun execute(newUpdateInterval: Float)
}
