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

/**
 * Datasource for working with clipboard
 */
interface ClipboardDataSource {
    /**
     * Add provided entry to the device's clipboard
     *
     * @param value String value of the item
     *
     * @return Whether or not clipboard operation was successful
     */
    fun copyToClipboard(value: CharSequence): Boolean
}
