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

package dev.fobo66.valiutchik.presentation

import androidx.lifecycle.ViewModel
import dev.fobo66.valiutchik.presentation.entity.LicensesState
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import fobo66.valiutchik.domain.usecases.LoadOpenSourceLicenses
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.map

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class, binding<ViewModel>())
class OpenSourceLicensesViewModelImpl(loadOpenSourceLicenses: LoadOpenSourceLicenses) :
    OpenSourceLicensesViewModel() {
    override val licensesState = loadOpenSourceLicenses.execute()
        .map { LicensesState(it.toImmutableList()) }
        .stateInWhileSubscribed(
            initialValue = LicensesState(
                persistentListOf()
            )
        )
}
