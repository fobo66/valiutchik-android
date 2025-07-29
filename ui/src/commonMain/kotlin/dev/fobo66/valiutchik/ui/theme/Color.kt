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

package dev.fobo66.valiutchik.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

internal const val DEFAULT_SEED_COLOR = 0xFF4CAF50

private const val MD_THEME_DARK_PRIMARY = 0xFF78DC77
private const val MD_THEME_DARK_ON_PRIMARY = 0xFF00390A
private const val MD_THEME_DARK_PRIMARY_CONTAINER = 0xFF005313
private const val MD_THEME_DARK_ON_PRIMARY_CONTAINER = 0xFF94F990
private const val MD_THEME_DARK_SECONDARY = 0xFFFFB870
private const val MD_THEME_DARK_ON_SECONDARY = 0xFF4A2800
private const val MD_THEME_DARK_SECONDARY_CONTAINER = 0xFF693C00
private const val MD_THEME_DARK_ON_SECONDARY_CONTAINER = 0xFFFFDCBE
private const val MD_THEME_DARK_TERTIARY = 0xFFF7BD48
private const val MD_THEME_DARK_ON_TERTIARY = 0xFF412D00
private const val MD_THEME_DARK_TERTIARY_CONTAINER = 0xFF5D4200
private const val MD_THEME_DARK_ON_TERTIARY_CONTAINER = 0xFFFFDEA6
private const val MD_THEME_DARK_ERROR = 0xFFFFB4AB
private const val MD_THEME_DARK_ERROR_CONTAINER = 0xFF93000A
private const val MD_THEME_DARK_ON_ERROR = 0xFF690005
private const val MD_THEME_DARK_ON_ERROR_CONTAINER = 0xFFFFDAD6
private const val MD_THEME_DARK_BACKGROUND = 0xFF1A1C19
private const val MD_THEME_DARK_ON_BACKGROUND = 0xFFE2E3DD
private const val MD_THEME_DARK_SURFACE = 0xFF1A1C19
private const val MD_THEME_DARK_ON_SURFACE = 0xFFE2E3DD
private const val MD_THEME_DARK_SURFACE_VARIANT = 0xFF424940
private const val MD_THEME_DARK_ON_SURFACE_VARIANT = 0xFFC2C9BD
private const val MD_THEME_DARK_OUTLINE = 0xFF8C9388
private const val MD_THEME_DARK_INVERSE_ON_SURFACE = 0xFF1A1C19
private const val MD_THEME_DARK_INVERSE_SURFACE = 0xFFE2E3DD
private const val MD_THEME_DARK_INVERSE_PRIMARY = 0xFF006E1C
private const val MD_THEME_DARK_SURFACE_TINT = 0xFF78DC77
private const val MD_THEME_DARK_OUTLINE_VARIANT = 0xFF424940
private const val MD_THEME_DARK_SCRIM = 0xFF000000

val DarkColors =
    darkColorScheme(
        primary = Color(MD_THEME_DARK_PRIMARY),
        onPrimary = Color(MD_THEME_DARK_ON_PRIMARY),
        primaryContainer = Color(MD_THEME_DARK_PRIMARY_CONTAINER),
        onPrimaryContainer = Color(MD_THEME_DARK_ON_PRIMARY_CONTAINER),
        secondary = Color(MD_THEME_DARK_SECONDARY),
        onSecondary = Color(MD_THEME_DARK_ON_SECONDARY),
        secondaryContainer = Color(MD_THEME_DARK_SECONDARY_CONTAINER),
        onSecondaryContainer = Color(MD_THEME_DARK_ON_SECONDARY_CONTAINER),
        tertiary = Color(MD_THEME_DARK_TERTIARY),
        onTertiary = Color(MD_THEME_DARK_ON_TERTIARY),
        tertiaryContainer = Color(MD_THEME_DARK_TERTIARY_CONTAINER),
        onTertiaryContainer = Color(MD_THEME_DARK_ON_TERTIARY_CONTAINER),
        error = Color(MD_THEME_DARK_ERROR),
        errorContainer = Color(MD_THEME_DARK_ERROR_CONTAINER),
        onError = Color(MD_THEME_DARK_ON_ERROR),
        onErrorContainer = Color(MD_THEME_DARK_ON_ERROR_CONTAINER),
        background = Color(MD_THEME_DARK_BACKGROUND),
        onBackground = Color(MD_THEME_DARK_ON_BACKGROUND),
        surface = Color(MD_THEME_DARK_SURFACE),
        onSurface = Color(MD_THEME_DARK_ON_SURFACE),
        surfaceVariant = Color(MD_THEME_DARK_SURFACE_VARIANT),
        onSurfaceVariant = Color(MD_THEME_DARK_ON_SURFACE_VARIANT),
        outline = Color(MD_THEME_DARK_OUTLINE),
        inverseOnSurface = Color(MD_THEME_DARK_INVERSE_ON_SURFACE),
        inverseSurface = Color(MD_THEME_DARK_INVERSE_SURFACE),
        inversePrimary = Color(MD_THEME_DARK_INVERSE_PRIMARY),
        surfaceTint = Color(MD_THEME_DARK_SURFACE_TINT),
        outlineVariant = Color(MD_THEME_DARK_OUTLINE_VARIANT),
        scrim = Color(MD_THEME_DARK_SCRIM)
    )

@Composable
internal expect fun resolveSeedColor(isDark: Boolean, isDynamic: Boolean = true): Color
