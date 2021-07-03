package fobo66.valiutchik.core.model.repository

interface LocationRepository {
  suspend fun resolveUserCity(): String
}
