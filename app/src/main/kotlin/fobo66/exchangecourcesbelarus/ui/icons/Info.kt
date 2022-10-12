/*
 *    Copyright 2022 Andrey Mukamolov
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
package fobo66.exchangecourcesbelarus.ui.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

val Info: ImageVector
  get() {
    if (_info != null) {
      return _info!!
    }
    _info = materialIcon(name = "Filled.Info") {
      materialPath {
        moveTo(12.0f, 2.0f)
        curveTo(6.48f, 2.0f, 2.0f, 6.48f, 2.0f, 12.0f)
        reflectiveCurveToRelative(4.48f, 10.0f, 10.0f, 10.0f)
        reflectiveCurveToRelative(10.0f, -4.48f, 10.0f, -10.0f)
        reflectiveCurveTo(17.52f, 2.0f, 12.0f, 2.0f)
        close()
        moveTo(13.0f, 17.0f)
        horizontalLineToRelative(-2.0f)
        verticalLineToRelative(-6.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(6.0f)
        close()
        moveTo(13.0f, 9.0f)
        horizontalLineToRelative(-2.0f)
        lineTo(11.0f, 7.0f)
        horizontalLineToRelative(2.0f)
        verticalLineToRelative(2.0f)
        close()
      }
    }
    return _info!!
  }

private var _info: ImageVector? = null
