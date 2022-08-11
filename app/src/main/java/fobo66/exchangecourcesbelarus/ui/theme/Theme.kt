package fobo66.exchangecourcesbelarus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
  primary = Green500,
  primaryContainer = Green200,
  secondary = Orange200
)

private val LightColorPalette = lightColorScheme(
  primary = Green500,
  primaryContainer = Green800,
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
fun ValiutchikTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }

  MaterialTheme(
    colorScheme = colors,
    typography = Typography,
    shapes = Shapes,
    content = content
  )
}
