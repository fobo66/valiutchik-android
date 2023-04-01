/*
 *    Copyright 2023 Andrey Mukamolov
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

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import javax.inject.Inject

class LicensesRepositoryImpl @Inject constructor(
  private val assetsDataSource: AssetsDataSource,
  private val moshi: Moshi
) : LicensesRepository {

  @OptIn(ExperimentalStdlibApi::class)
  private val jsonAdapter: JsonAdapter<List<OpenSourceLicensesItem>> by lazy {
    moshi.adapter()
  }

  override fun loadLicenses(): List<OpenSourceLicensesItem> {
    val licensesFile = assetsDataSource.loadFile("open_source_licenses.json")
    return jsonAdapter.fromJson(licensesFile) ?: emptyList()
  }
}
