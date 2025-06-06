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

import fobo66.valiutchik.core.model.datasource.PreferencesDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakePreferenceDataSource : PreferencesDataSource {
  var string = "default"
  var int = 3

  override suspend fun loadString(
    key: String,
    defaultValue: String,
  ): String = string

  override suspend fun saveString(
    key: String,
    value: String,
  ) = Unit

  override suspend fun loadInt(
    key: String,
    defaultValue: Int,
  ): Int = int

  override fun observeString(
    key: String,
    defaultValue: String,
  ): Flow<String> = flowOf(string)

  override fun observeInt(
    key: String,
    defaultValue: Int,
  ): Flow<Int> = flowOf(int)

  override suspend fun saveInt(
    key: String,
    value: Int,
  ) = Unit
}
