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

package fobo66.valiutchik.api

import androidx.collection.scatterSetOf
import fobo66.valiutchik.api.entity.Bank

/**
 * XML parser for [MyFIN](myfin.by) dataset
 */
abstract class CurrencyRatesParser {
    internal val neededTagNames by lazy(LazyThreadSafetyMode.NONE) {
        scatterSetOf(
            TAG_NAME_BANK_ID,
            TAG_NAME_FILIAL_ID,
            TAG_NAME_DATE,
            TAG_NAME_BANK_NAME,
            TAG_NAME_USD_BUY,
            TAG_NAME_USD_SELL,
            TAG_NAME_EUR_BUY,
            TAG_NAME_EUR_SELL,
            TAG_NAME_RUR_BUY,
            TAG_NAME_RUR_SELL,
            TAG_NAME_PLN_BUY,
            TAG_NAME_PLN_SELL,
            TAG_NAME_UAH_BUY,
            TAG_NAME_UAH_SELL
        )
    }

    abstract fun parse(body: String): Set<Bank>
}
