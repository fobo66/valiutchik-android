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

package fobo66.exchangecourcesbelarus.mcp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appfunctions.AppFunction
import androidx.appfunctions.AppFunctionService
import androidx.appfunctions.AppFunctionServiceEntryPoint
import fobo66.valiutchik.domain.usecases.LoadExchangeRates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
@AppFunctionServiceEntryPoint(
    serviceName = "RatesAppFunctionService",
    appFunctionXmlFileName = "rates_app_function_service"
)
abstract class ExchangeRatesAppFunctionsService : AppFunctionService() {
    private val loadExchangeRates: LoadExchangeRates by inject()

    /**
     * Load exchange rate for the provided currency for today.
     *
     * @param currency Currency code in ISO 4217 format. Example: USD, EUR
     * @return exchange rate in BYN, or null if the currency is not supported
     */
    @AppFunction(isDescribedByKDoc = true)
    suspend fun loadExchangeRateForCurrency(currency: String): String? =
        withContext(Dispatchers.Default) {
            val rate = loadExchangeRates()
                .map { it.firstOrNull { it.currencyName == currency } }
                .first()

            rate?.rateValue
        }
}
