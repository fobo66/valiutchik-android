/*
 *    Copyright 2026 Andrey Mukamolov
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

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.DependencyGraph
import fobo66.valiutchik.core.model.datasource.AssetsDataSource
import fobo66.valiutchik.core.model.datasource.AssetsDataSourceImpl
import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import fobo66.valiutchik.core.model.datasource.ClipboardDataSourceImpl
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.FormattingDataSourceImpl
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.IntentDataSourceImpl
import fobo66.valiutchik.core.model.datasource.LocaleDataSource
import fobo66.valiutchik.core.model.datasource.LocaleDataSourceImpl
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceImpl
import fobo66.valiutchik.core.model.datasource.UriDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSourceImpl

@DependencyGraph(AppScope::class)
interface AndroidSystemModule : SystemModule {
    @Binds
    val AssetsDataSourceImpl.bind: AssetsDataSource

    @Binds
    val ClipboardDataSourceImpl.bind: ClipboardDataSource

    @Binds
    val FormattingDataSourceImpl.bind: FormattingDataSource

    @Binds
    val IntentDataSourceImpl.bind: IntentDataSource

    @Binds
    val LocaleDataSourceImpl.bind: LocaleDataSource

    @Binds
    val LocationDataSourceImpl.bind: LocationDataSource

    @Binds
    val UriDataSourceImpl.bind: UriDataSource
}
