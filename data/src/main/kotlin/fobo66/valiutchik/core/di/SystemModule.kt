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

package fobo66.valiutchik.core.di

import androidx.core.app.LocaleManagerCompat
import java.util.Locale
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val systemModule =
    module {
        single {
            androidContext().assets
        }

        single {
            val applicationLocales = LocaleManagerCompat.getApplicationLocales(androidContext())
            val systemLocales = LocaleManagerCompat.getSystemLocales(androidContext())
            val currentLocale =
                if (applicationLocales.isEmpty) {
                    systemLocales.get(0)
                } else {
                    applicationLocales.get(0)
                }
            currentLocale ?: Locale.getDefault()
        }
    }
