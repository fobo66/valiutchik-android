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

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.LocaleList
import androidx.core.app.LocaleManagerCompat
import androidx.core.content.IntentCompat
import fobo66.valiutchik.core.entities.LanguageTag
import io.github.aakira.napier.Napier
import java.util.Locale
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LocaleDataSourceImpl(private val context: Context) : LocaleDataSource {
    override val locale: Flow<LanguageTag> = callbackFlow {
        val localeReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Napier.d { "Locale was changed" }
                if (context.packageName == intent.getStringExtra(Intent.EXTRA_PACKAGE_NAME)) {
                    val newLocaleList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        IntentCompat.getParcelableExtra(
                            intent,
                            Intent.EXTRA_LOCALE_LIST,
                            LocaleList::class.java
                        ) ?: LocaleList.getEmptyLocaleList()
                    } else {
                        LocaleList.getEmptyLocaleList()
                    }
                    val locale = newLocaleList.get(0) ?: Locale.getDefault()
                    channel.trySend(locale.toLanguageTag())
                }
            }
        }

        val applicationLocales = LocaleManagerCompat.getApplicationLocales(context)
        val systemLocales = LocaleManagerCompat.getSystemLocales(context)
        val currentLocale =
            if (applicationLocales.isEmpty) {
                systemLocales.get(0)
            } else {
                applicationLocales.get(0)
            }

        Napier.d {
            "Starting with locale $currentLocale"
        }
        channel.send((currentLocale ?: Locale.getDefault()).toLanguageTag())

        context.registerReceiver(localeReceiver, IntentFilter(Intent.ACTION_LOCALE_CHANGED))

        awaitClose { context.unregisterReceiver(localeReceiver) }
    }
}
