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

import io.github.aakira.napier.Napier
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class ClipboardDataSourceJvmImpl : ClipboardDataSource {
    override fun copyToClipboard(label: CharSequence, value: CharSequence): Boolean {
        Toolkit.getDefaultToolkit()
            .systemClipboard
            .setContents(
                StringSelection(value.toString()),
                null
            )

        Napier.v { "Copied $label: $value" }

        return true
    }
}
