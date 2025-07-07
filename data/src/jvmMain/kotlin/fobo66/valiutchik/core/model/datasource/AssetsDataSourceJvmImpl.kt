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

import kotlinx.io.Source
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.files.FileNotFoundException

class AssetsDataSourceJvmImpl : AssetsDataSource {
    /**
     * Load file from assets
     *
     * @param fileName Name of the asset file. It should exist in the assets
     */
    override fun loadFile(fileName: String): Source =
        javaClass.classLoader.getResourceAsStream(fileName)?.asSource()?.buffered()
            ?: throw FileNotFoundException("Asset not found")
}
