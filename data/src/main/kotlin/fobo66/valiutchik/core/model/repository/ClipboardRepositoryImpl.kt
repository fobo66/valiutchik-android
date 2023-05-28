/*
 *    Copyright 2022 Andrey Mukamolov
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

import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import org.koin.core.annotation.Single
import timber.log.Timber

@Single
class ClipboardRepositoryImpl(
  private val clipboardDataSource: ClipboardDataSource
) : ClipboardRepository {

  override fun copyToClipboard(label: CharSequence, value: CharSequence) {
    Timber.v("Copying to clipboard: label = %s, value = %s", label, value)
    if (clipboardDataSource.copyToClipboard(label, value)) {
      Timber.v("Copied successfully")
    } else {
      Timber.v("Copying failed")
    }
  }
}
