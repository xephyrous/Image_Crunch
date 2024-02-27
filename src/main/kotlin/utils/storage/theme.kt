package utils.storage

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

// noodling away color choices,
// 18 colors per theme: they make it to wacky things
// note: also add default color values to the selections
// Header, Text 1, Text 2, Greyed Text, Icon, BG Grad 1, BG Grad 2, BG Grad 3, Card Grad 1, Card Grad 2, Button, FAB,
// TextField On Text, TextField Off Text, TextField BG, TextField Cursor, TextField Focused, TextField Unfocused, Border

// TODO: Possibly convert theme storage to Pkl
// TODO: add more colors for sliders and switches :P

/**
 * Dark Application Theme
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF2B2D30"> > Header Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF26282E"> > Body Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFBFC1C7"> > Text Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF7A7E85"> > Icon Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF1E1F22"> > Button Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF2B2D30"> > Menu Color </p>
*/
val darkThemes = listOf(
    Color(0xFF2B2D30),
    Color(0xFFBFC1C7),
    Color(0xFFBFC1C7),
    Color(0xFFBFC1C7),
    Color(0xFF7A7E85),
    Color(0xFF26282E),
    Color(0xFF26282E),
    Color(0xFF26282E),
    Color(0xFF2B2D30),
    Color(0xFF2B2D30),
    Color(0xFF1E1F22),
    Color(0xFF1E1F22),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFF000000)
)


/**
 * Light Application Theme
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFFFFFFF"> > Header Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFFFFFFF"> > Body Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFFFFB00"> > Text Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF00FF00"> > Icon Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFFFFFFF"> > Button Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFFFFFFF"> > Menu Color </p>
*/
val lightThemes = listOf(
    Color(0xFFf638fd),
    Color(0xFF28d428),
    Color(0xFF799784),
    Color(0xFFd13f6f),
    Color(0xFFbdc0ca),
    Color(0xFF90857f),
    Color(0xFFd0e7ed),
    Color(0xFF8da7b7),
    Color(0xFF9e5004),
    Color(0xFFc2947b),
    Color(0xFFf68413),
    Color(0xFF544cdf),
    Color(0xFFac9ca7),
    Color(0xFF4646e2),
    Color(0xFF1b5e20),
    Color(0xFF1b5e20),
    Color(0xFFff4081),
    Color(0xFFf1919b),
    Color(0xFF000000)
)

/**
 * Celeste Application Theme
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF42033D"> > Header Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF3c3147"> > Body Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF854798"> > Text Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF7C238C"> > Icon Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF7C72AD"> > Button Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF680E4B"> > Menu Color </p>
*/
val celesteThemes = listOf(
    Color(0xFF58326C),
    Color(0xFF9C40CB),
    Color(0xFF7C2FA4),
    Color(0xFF65307F),
    Color(0xFFE5B1FF),
    Color(0xFF7926E0),
    Color(0xFF8266F2),
    Color(0xFFBC73C8),
    Color(0xFF85039B),
    Color(0xFF9114A6),
    Color(0xFF621170),
    Color(0xFF621170),
    Color(0xFFB945CC),
    Color(0xFFB75CC6),
    Color(0xFFB218CA),
    Color(0xFFCF6EDF),
    Color(0xFFEBB8F3),
    Color(0xFFA715E7),
    Color(0xFF000000)
)

/**
 * Aqueous Application Theme
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF2b2caa"> > Header Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF89c4f4"> > Body Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFcdd1e4"> > Text Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFFe4f1fe"> > Icon Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF038aff"> > Button Color </p>
 * <p style="background-color:#ffffff;font-weight:bold"><font color="0xFF4871f7"> > Menu Color </p>
*/
val aqueousThemes = listOf(
    Color(0xFF67C6FF),
    Color(0xFF003C60),
    Color(0xFF0A5A8A),
    Color(0xFF42ADED),
    Color(0xFF627A88),
    Color(0xFF77C5F3),
    Color(0xFF6D9AB5),
    Color(0xFF77A9F3),
    Color(0xFF8FDEEE),
    Color(0xFF76C8D8),
    Color(0xFFACEAF6),
    Color(0xFFACEAF6),
    Color(0xFF799784),
    Color(0xFF42ADED),
    Color(0xFFADFBFE),
    Color(0xFF176CB9),
    Color(0xFF0B9AB6),
    Color(0xFF86E2F4),
    Color(0xFF000000)
)

// use for persistent data storage
val themeTypeMap = mapOf(darkThemes to 0, lightThemes to 1, celesteThemes to 2, aqueousThemes to 3)
