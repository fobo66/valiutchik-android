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

package dev.fobo66.core.data.testing.fake

import fobo66.valiutchik.api.GeocodingDataSource
import fobo66.valiutchik.api.entity.Feature
import fobo66.valiutchik.api.entity.GeocodingFailedException
import fobo66.valiutchik.api.entity.Properties

class FakeGeocodingDataSource : GeocodingDataSource {
    var showError = false
    var unexpectedError = false

    private val searchResult = Feature(Properties(city = "fake"))

    override suspend fun findPlace(latitude: Double, longitude: Double): List<Feature> = when {
        showError -> throw GeocodingFailedException(Throwable("Yikes!"))
        unexpectedError -> throw NullPointerException("Yikes!")
        else -> listOf(searchResult)
    }
}
