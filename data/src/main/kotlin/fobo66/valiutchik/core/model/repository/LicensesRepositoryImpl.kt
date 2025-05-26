/*
 *    Copyright 2025 Andrey Mukamolov
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
import fobo66.valiutchik.core.model.datasource.JsonDataSource
import java.nio.charset.Charset

class LicensesRepositoryImpl(
    private val assetsDataSource: AssetsDataSource,
    private val jsonDataSource: JsonDataSource
) : LicensesRepository {

    override fun loadLicenses(): List<OpenSourceLicensesItem> {
        val licensesFile =
            assetsDataSource.loadFile(
                "open_source_licenses.json"
            ).readString(Charset.defaultCharset())
        return jsonDataSource.decodeLicenses(licensesFile) ?: emptyList()
    }
}
