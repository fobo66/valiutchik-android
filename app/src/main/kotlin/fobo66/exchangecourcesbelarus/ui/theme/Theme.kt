/*
 *    Copyright 2023 Andrey Mukamolov
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

package fobo66.exchangecourcesbelarus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme

private val DarkColorPalette = darkColorScheme(
  primary = Green800,
  primaryContainer = Green500,
  secondary = Orange200
)

private val LightColorPalette = lightColorScheme(
  primary = Green500,
  primaryContainer = Green200,
  secondary = Orange500

  /* Other default colors to override
  background = Color.White,
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.Black,
  onBackground = Color.Black,
  onSurface = Color.Black,
  */
)

@Composable
fun ValiutchikTheme(
  isDarkTheme: Boolean = isSystemInDarkTheme(),
  isDynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

  val colorScheme = when {
    dynamicColor && isDarkTheme -> {
      dynamicDarkColorScheme(LocalContext.current)
    }

    dynamicColor && !isDarkTheme -> {
      dynamicLightColorScheme(LocalContext.current)
    }

    isDarkTheme -> DarkColorPalette
    else -> LightColorPalette
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}

@Composable
fun ValiutchikWidgetTheme(
  content: @GlanceComposable @Composable () -> Unit
) {
  GlanceTheme(content = content)
}
