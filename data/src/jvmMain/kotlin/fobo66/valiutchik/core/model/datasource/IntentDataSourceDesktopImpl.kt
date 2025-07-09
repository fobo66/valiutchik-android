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

import com.eygraber.uri.Uri
import com.eygraber.uri.toURI
import io.github.aakira.napier.Napier
import java.awt.Desktop
import java.awt.HeadlessException
import kotlinx.io.IOException

class IntentDataSourceDesktopImpl : IntentDataSource {
    override fun resolveIntent(intentUri: Uri): Boolean = try {
        val desktop = Desktop.getDesktop()
        desktop.browse(intentUri.toURI())
        true
    } catch (e: UnsupportedOperationException) {
        Napier.e(e) {
            "Opening browser is not supported"
        }
        false
    } catch (e: IOException) {
        Napier.e(e) {
            "Failed to open browser"
        }
        false
    } catch (e: HeadlessException) {
        Napier.e(e) {
            "Cannot open browser in headless mode"
        }
        false
    }

    override fun checkIntentUri(uri: Uri): Boolean = Desktop.isDesktopSupported() &&
        Desktop.getDesktop()
            .isSupported(Desktop.Action.BROWSE)
}
