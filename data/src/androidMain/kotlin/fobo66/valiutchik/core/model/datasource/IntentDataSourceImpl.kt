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

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import com.eygraber.uri.Uri
import com.eygraber.uri.toAndroidUri
import io.github.aakira.napier.Napier
import java.net.URISyntaxException

class IntentDataSourceImpl(private val context: Context) : IntentDataSource {
    override fun resolveIntent(intentUri: Uri): Boolean = try {
        context.startActivity(
            Intent.createChooser(
                Intent(
                    Intent.ACTION_VIEW,
                    intentUri.toAndroidUri()
                ),
                "Open map"
            )
        )
        true
    } catch (e: ActivityNotFoundException) {
        Napier.e(e) {
            "Intent cannot be resolved"
        }
        false
    } catch (e: URISyntaxException) {
        Napier.e(e) {
            "Intent URI is malformed"
        }
        false
    }

    override fun checkIntentUri(uri: Uri): Boolean = Intent(
        Intent.ACTION_VIEW,
        uri.toAndroidUri()
    ).resolveActivity(context.packageManager) != null
}
