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

package fobo66.exchangecourcesbelarus.init

import android.content.Context
import androidx.startup.Initializer
import dev.fobo66.valiutchik.presentation.di.viewModelsModule
import fobo66.exchangecourcesbelarus.di.workersModule
import fobo66.valiutchik.domain.di.domainModule
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class KoinInitializer : Initializer<KoinApplication> {
    override fun create(context: Context): KoinApplication = startKoin {
        androidContext(context)
        workManagerFactory()
        modules(viewModelsModule, domainModule, workersModule)
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}
