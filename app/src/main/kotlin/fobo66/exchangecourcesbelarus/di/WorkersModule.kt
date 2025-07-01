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

package fobo66.exchangecourcesbelarus.di

import androidx.work.WorkManager
import fobo66.exchangecourcesbelarus.work.RatesRefreshWorker
import fobo66.exchangecourcesbelarus.work.RefreshInteractorWorkManagerImpl
import fobo66.valiutchik.domain.usecases.RefreshInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val workersModule = module {
    single { WorkManager.getInstance(androidContext()) }
    single<RefreshInteractor> { RefreshInteractorWorkManagerImpl(get(), get()) }
    worker { RatesRefreshWorker(get(), get(), androidContext(), get()) }
}
