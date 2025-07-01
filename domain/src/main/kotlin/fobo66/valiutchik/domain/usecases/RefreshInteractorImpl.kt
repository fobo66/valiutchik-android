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

package fobo66.valiutchik.domain.usecases

import fobo66.valiutchik.core.entities.CurrencyRatesLoadFailedException
import io.github.aakira.napier.Napier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RefreshInteractorImpl(
    private val refreshExchangeRates: ForceRefreshExchangeRates,
    private val refreshExchangeRatesForDefaultCity: ForceRefreshExchangeRatesForDefaultCity
) : RefreshInteractor {
    private val _isRefreshInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override val isRefreshInProgress: Flow<Boolean> = _isRefreshInProgress.asStateFlow()

    override suspend fun handleRefresh(isLocationAvailable: Boolean, updateInterval: Long) = try {
        _isRefreshInProgress.emit(true)

        if (isLocationAvailable) {
            refreshExchangeRates.execute()
        } else {
            refreshExchangeRatesForDefaultCity.execute()
        }
    } catch (e: CurrencyRatesLoadFailedException) {
        Napier.e("Refresh failed", e)
    } finally {
        _isRefreshInProgress.emit(false)
    }
}
