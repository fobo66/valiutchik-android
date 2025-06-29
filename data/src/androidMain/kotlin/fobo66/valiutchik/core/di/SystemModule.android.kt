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
import fobo66.valiutchik.api.di.Dispatcher
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import fobo66.valiutchik.core.model.datasource.AssetsDataSourceImpl
import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import fobo66.valiutchik.core.model.datasource.ClipboardDataSourceImpl
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.FormattingDataSourceImpl
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.IntentDataSourceImpl
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceImpl
import java.util.Locale
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

actual val systemModule: Module = module {
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

    single<AssetsDataSource> {
        AssetsDataSourceImpl(get())
    }

    single<ClipboardDataSource> {
        ClipboardDataSourceImpl(androidContext())
    }

    single<IntentDataSource> {
        IntentDataSourceImpl(androidContext())
    }

    single<FormattingDataSource> {
        FormattingDataSourceImpl(get(), get())
    }

    single<LocationDataSource> {
        LocationDataSourceImpl(androidContext(), get(qualifier(Dispatcher.BACKGROUND)))
    }
}
