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
import fobo66.valiutchik.core.model.datasource.AssetsDataSourceWebImpl
import fobo66.valiutchik.core.model.datasource.ClipboardDataSource
import fobo66.valiutchik.core.model.datasource.ClipboardDataSourceWebImpl
import fobo66.valiutchik.core.model.datasource.FormattingDataSource
import fobo66.valiutchik.core.model.datasource.FormattingDataSourceWebImpl
import fobo66.valiutchik.core.model.datasource.IntentDataSource
import fobo66.valiutchik.core.model.datasource.IntentDataSourceWebImpl
import fobo66.valiutchik.core.model.datasource.LocaleDataSource
import fobo66.valiutchik.core.model.datasource.LocaleDataSourceWebImpl
import fobo66.valiutchik.core.model.datasource.LocationDataSource
import fobo66.valiutchik.core.model.datasource.LocationDataSourceIpImpl
import fobo66.valiutchik.core.model.datasource.UriDataSource
import fobo66.valiutchik.core.model.datasource.UriDataSourceExternalImpl

@DependencyGraph(AppScope::class)
interface WebSystemModule : SystemModule {
    @Binds
    val AssetsDataSourceWebImpl.bind: AssetsDataSource

    @Binds
    val ClipboardDataSourceWebImpl.bind: ClipboardDataSource

    @Binds
    val FormattingDataSourceWebImpl.bind: FormattingDataSource

    @Binds
    val IntentDataSourceWebImpl.bind: IntentDataSource

    @Binds
    val LocaleDataSourceWebImpl.bind: LocaleDataSource

    @Binds
    val LocationDataSourceIpImpl.bind: LocationDataSource

    @Binds
    val UriDataSourceExternalImpl.bind: UriDataSource
}
