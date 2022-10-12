package fobo66.valiutchik.domain.usecases

interface UpdateDefaultCityPreference {
  suspend fun execute(newDefaultCity: String)
}
