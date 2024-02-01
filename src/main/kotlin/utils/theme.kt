package utils

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

/**
 * Represents the application's main font face, linked with assets
 */
object AppFont {
    val Metropolis = FontFamily(
        Font(resource = "Metropolis-Black.otf", weight = FontWeight.Black),
        Font(resource = "Metropolis-BlackItalic.otf", weight = FontWeight.Black, style = FontStyle.Italic),
        Font(resource = "Metropolis-Bold.otf", weight = FontWeight.Bold),
        Font(resource = "Metropolis-BoldItalic.otf", weight = FontWeight.Bold, style = FontStyle.Italic),
        Font(resource = "Metropolis-ExtraBold.otf", weight = FontWeight.ExtraBold),
        Font(resource = "Metropolis-ExtraBoldItalic.otf", weight = FontWeight.ExtraBold, style = FontStyle.Italic),
        Font(resource = "Metropolis-ExtraLight.otf", weight = FontWeight.ExtraLight),
        Font(resource = "Metropolis-ExtraLightItalic.otf", weight = FontWeight.ExtraLight, style = FontStyle.Italic),
        Font(resource = "Metropolis-Light.otf", weight = FontWeight.Light),
        Font(resource = "Metropolis-LightItalic.otf", weight = FontWeight.Light, style = FontStyle.Italic),
        Font(resource = "Metropolis-Medium.otf", weight = FontWeight.Medium),
        Font(resource = "Metropolis-MediumItalic.otf", weight = FontWeight.Medium, style = FontStyle.Italic),
        Font(resource = "Metropolis-Regular.otf", weight = FontWeight.Normal),
        Font(resource = "Metropolis-RegularItalic.otf", weight = FontWeight.Normal, style = FontStyle.Italic),
        Font(resource = "Metropolis-SemiBold.otf", weight = FontWeight.SemiBold),
        Font(resource = "Metropolis-SemiBoldItalic.otf", weight = FontWeight.SemiBold, style = FontStyle.Italic),
        Font(resource = "Metropolis-Thin.otf", weight = FontWeight.Thin),
        Font(resource = "Metropolis-ThinItalic.otf", weight = FontWeight.Thin, style = FontStyle.Italic),
    )
}

/**
 * Represents the application's Typography, set for each type of text
 */
val appTypography = Typography(
    defaultFontFamily = AppFont.Metropolis,
    body1 = TextStyle(fontWeight = FontWeight.Normal),
    body2 = TextStyle(fontWeight = FontWeight.Medium),
    button = TextStyle(fontWeight = FontWeight.SemiBold),
    caption = TextStyle(fontWeight = FontWeight.Light),
    h1 = TextStyle(fontWeight = FontWeight.Black),
    h2 = TextStyle(fontWeight = FontWeight.ExtraBold),
    h3 = TextStyle(fontWeight = FontWeight.Bold),
    h4 = TextStyle(fontWeight = FontWeight.SemiBold),
    h5 = TextStyle(fontWeight = FontWeight.Medium),
    h6 = TextStyle(fontWeight = FontWeight.Normal),
    overline = TextStyle(fontWeight = FontWeight.Light),
    subtitle1 = TextStyle(fontWeight = FontWeight.Normal),
    subtitle2 = TextStyle(fontWeight = FontWeight.Medium)
)

/**
 * A MaterialTheme wrapper for the main application,
 * applies the typography to all text
 */
@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = appTypography,
        content = content
    )
}

/* THEME MARKER
Header
Body
Text
Icon
Button
Menu
*/
val darkThemes = listOf(
    Color(0xFF2B2D30),
    Color(0xFF26282E),
    Color(0xFFBFC1C7),
    Color(0xFF7A7E85),
    Color(0xFF1E1F22),
    Color(0xFF2B2D30)
)

/* THEME MARKER
Header
Body
Text
Icon
Button
Menu
*/
val lightThemes = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFB00),
    Color(0xFF00FF00),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF)
)

/* THEME MARKER
Header
Body
Text
Icon
Button
Menu
*/
val celesteThemes = listOf(
    Color(0xFF42033D),
    Color(0xFF3c3147),
    Color(0xFF854798),
    Color(0xFF7C238C),
    Color(0xFF7C72AD),
    Color(0xFF680E4B)
)

/* THEME MARKER
Header
Body
Text
Icon
Button
Menu
*/
val aqueousThemes = listOf(
    Color(0xFF2b2caa),
    Color(0xFF89c4f4),
    Color(0xFFcdd1e4),
    Color(0xFFe4f1fe),
    Color(0xFF038aff),
    Color(0xFF4871f7)
)

// use for persistent data storage
val themeTypeMap = mapOf(darkThemes to 0, lightThemes to 1, celesteThemes to 2, aqueousThemes to 3)