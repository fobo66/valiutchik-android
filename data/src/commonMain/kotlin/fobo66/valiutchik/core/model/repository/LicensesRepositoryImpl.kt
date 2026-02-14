/*
 *    Copyright 2026 Andrey Mukamolov
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package fobo66.valiutchik.core.model.repository

import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import fobo66.valiutchik.core.model.datasource.LicensesDataSource

class LicensesRepositoryImpl(
    private val assetsDataSource: AssetsDataSource,
    private val licensesDataSource: LicensesDataSource
) : LicensesRepository {

    override fun loadLicenses(): List<OpenSourceLicensesItem> = assetsDataSource.loadFile(
        "open_source_licenses.json"
    ).use { licensesFile ->
        return licensesDataSource.decodeLicenses(licensesFile)
            ?.map { library ->
                OpenSourceLicensesItem(
                    dependency = library.uniqueId,
                    description = library.description,
                    developers = library.developers.mapNotNull { it.name },
                    licenses = library.licenses.map { it.name },
                    project = library.name,
                    url = library.website,
                    version = library.artifactVersion.orEmpty(),
                    year = library.licenses.find { !it.year.isNullOrEmpty() }?.year
                )
            } ?: emptyList()
    }
}
