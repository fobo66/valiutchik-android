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

package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.model.repository.LocaleRepository
import fobo66.valiutchik.core.model.repository.PreferenceRepository
import fobo66.valiutchik.domain.entities.CityPreference
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class LoadDefaultCityPreferenceValuesImpl(
    private val localeRepository: LocaleRepository,
    private val preferenceRepository: PreferenceRepository
) : LoadDefaultCityPreferenceValues {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(): Flow<List<CityPreference>> = localeRepository.loadLocale()
        .flatMapLatest { languageTag ->
            preferenceRepository.observeCities()
                .map { cities ->
                    cities.map { city ->
                        val label = when (languageTag) {
                            "be" -> city.belName
                            "ru" -> city.rusName
                            else -> city.name
                        }

                        CityPreference(label = label, preferenceValue = city.id)
                    }
                }
        }
}
