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

import com.ibm.icu.util.ULocale
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import fobo66.valiutchik.core.model.datasource.AssetsDataSourceJvmImpl
import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import fobo66.valiutchik.core.model.datasource.ClipboardDataSourceJvmImpl
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.FormattingDataSourceIcuImpl
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.IntentDataSourceDesktopImpl
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceStubImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val systemModule: Module = module {
    single {
        ULocale.getDefault()
    }

    single<FormattingDataSource> {
        FormattingDataSourceIcuImpl(get(), get())
    }

    single<LocationDataSource> { LocationDataSourceStubImpl() }

    single<IntentDataSource> { IntentDataSourceDesktopImpl() }

    single<ClipboardDataSource> { ClipboardDataSourceJvmImpl() }

    single<AssetsDataSource> { AssetsDataSourceJvmImpl() }
}
