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

package fobo66.exchangecourcesbelarus.di

import android.content.Context
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.Includes
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.viewmodel.ViewModelGraph
import fobo66.valiutchik.core.di.AndroidThirdPartyModule
import fobo66.valiutchik.domain.di.DomainModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@DependencyGraph(AppScope::class, bindingContainers = [DomainModule::class])
interface AppGraph :
    MetroAppComponentProviders,
    ViewModelGraph {
    val context: Context

    @Provides
    @SingleIn(AppScope::class)
    fun provideCircuit(
        presenterFactories: Set<Presenter.Factory>,
        uiFactories: Set<Ui.Factory>
    ): Circuit = Circuit.Builder()
        .addPresenterFactories(presenterFactories)
        .addUiFactories(uiFactories)
        .build()

    @Provides
    fun provideDispatcher(): CoroutineDispatcher = Dispatchers.Default

    @DependencyGraph.Factory
    fun interface Factory {
        fun create(
            @Provides context: Context,
            @Includes thirdPartyModule: AndroidThirdPartyModule
        ): AppGraph
    }
}
