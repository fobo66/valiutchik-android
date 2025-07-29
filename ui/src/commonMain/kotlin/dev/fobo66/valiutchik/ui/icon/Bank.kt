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

@file:Suppress("MagicNumber") // ok for icons

package dev.fobo66.valiutchik.ui.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Icons.Filled.Bank: ImageVector
    get() {
        if (_bank != null) {
            return _bank!!
        }
        _bank = materialIcon(name = "Filled.AccountBalance") {
            materialPath {
                moveTo(4.0f, 10.0f)
                horizontalLineToRelative(3.0f)
                verticalLineToRelative(7.0f)
                horizontalLineToRelative(-3.0f)
                close()
            }
            materialPath {
                moveTo(10.5f, 10.0f)
                horizontalLineToRelative(3.0f)
                verticalLineToRelative(7.0f)
                horizontalLineToRelative(-3.0f)
                close()
            }
            materialPath {
                moveTo(2.0f, 19.0f)
                horizontalLineToRelative(20.0f)
                verticalLineToRelative(3.0f)
                horizontalLineToRelative(-20.0f)
                close()
            }
            materialPath {
                moveTo(17.0f, 10.0f)
                horizontalLineToRelative(3.0f)
                verticalLineToRelative(7.0f)
                horizontalLineToRelative(-3.0f)
                close()
            }
            materialPath {
                moveTo(12.0f, 1.0f)
                lineToRelative(-10.0f, 5.0f)
                lineToRelative(0.0f, 2.0f)
                lineToRelative(20.0f, 0.0f)
                lineToRelative(0.0f, -2.0f)
                close()
            }
        }
        return _bank!!
    }

@Suppress("ktlint:standard:backing-property-naming") // ok for icons
private var _bank: ImageVector? = null
