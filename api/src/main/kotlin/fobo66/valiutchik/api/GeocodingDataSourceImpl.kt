/*
 *    Copyright 2024 Andrey Mukamolov
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

package fobo66.valiutchik.api

import fobo66.valiutchik.api.entity.Feature
import fobo66.valiutchik.api.entity.GeocodingFailedException
import fobo66.valiutchik.api.entity.GeocodingResult
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.io.IOException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

private const val GEOCODING_API_URL = "https://api.geoapify.com/v1/geocode/reverse"

class GeocodingDataSourceImpl(
  private val httpClient: HttpClient,
  private val apiKey: String,
  private val ioDispatcher: CoroutineDispatcher
) : GeocodingDataSource {
  override suspend fun findPlace(
    latitude: Double,
    longitude: Double
  ): List<Feature> = withContext(ioDispatcher) {
    try {
      val result: GeocodingResult = httpClient.get(GEOCODING_API_URL) {
        parameter("apiKey", apiKey)
        parameter("type", "city")
        parameter("lat", latitude)
        parameter("lon", longitude)
      }.body()
      result.features
    } catch (e: ResponseException) {
      Napier.e(e) {
        "Geocoding API request failed"
      }
      throw GeocodingFailedException(e.message)
    } catch (e: IOException) {
      Napier.e(e) {
        "Unexpected issue happened during geocoding request"
      }
      throw GeocodingFailedException(e.message)
    }
  }
}
