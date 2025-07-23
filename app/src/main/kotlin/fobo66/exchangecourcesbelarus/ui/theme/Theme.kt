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

package fobo66.exchangecourcesbelarus.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.expressiveLightColorScheme
import androidx.compose.runtime.Composable
import androidx.glance.GlanceComposable
import androidx.glance.GlanceTheme
import androidx.glance.material3.ColorProviders
import dev.fobo66.valiutchik.ui.theme.AppTheme
import dev.fobo66.valiutchik.ui.theme.DarkColors

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private val WidgetColors =
    ColorProviders(
        light = expressiveLightColorScheme(),
        dark = DarkColors
    )

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ValiutchikTheme(isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    AppTheme(
        isDarkTheme = isDarkTheme,
        content = content
    )
}

@Composable
fun ValiutchikWidgetTheme(
    isDynamicColor: Boolean = true,
    content:
    @GlanceComposable
    @Composable () -> Unit
) {
    val dynamicColor = isDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    GlanceTheme(
        colors =
        if (dynamicColor) {
            GlanceTheme.colors
        } else {
            WidgetColors
        },
        content = content
    )
}
