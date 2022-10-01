package fobo66.valiutchik.core.usecases

interface UpdateDefaultCityPreference {
  suspend fun execute(newDefaultCity: String)
}
