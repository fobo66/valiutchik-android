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

package fobo66.valiutchik.core.model.datasource

import fobo66.valiutchik.core.entities.OpenSourceLicensesItem
import io.github.aakira.napier.Napier
import kotlinx.io.IOException
import kotlinx.io.Source
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.io.decodeFromSource

class JsonDataSourceImpl(private val json: Json) : JsonDataSource {
    @OptIn(ExperimentalSerializationApi::class)
    override fun decodeLicenses(jsonSource: Source): List<OpenSourceLicensesItem>? = try {
        json.decodeFromSource(jsonSource)
    } catch (e: SerializationException) {
        Napier.e(e) {
            "Data format issue"
        }
        null
    } catch (e: IOException) {
        Napier.e(e) {
            "File read issue"
        }
        null
    }
}
