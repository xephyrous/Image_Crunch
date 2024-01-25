package utils

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

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

val typography = Typography(
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