package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.entities.OpenSourceLicensesItem

interface LicensesRepository {
  fun loadLicenses(): List<OpenSourceLicensesItem>
}
