package utils

import androidx.compose.ui.graphics.Color
import java.awt.Dimension
import java.io.File

var loadedImageSize: Dimension = Dimension()
var selectedImage: File? = null
var compactExport: Boolean = true

val generatorTypeMap = mapOf("Square Generator" to 0)
var generatorType: Int = 0 // Temporary storage for gen type, change to whatever idc.

val themeTypeMap = mapOf("Dark" to 0, "Light" to 1, "Celeste" to 2, "Aqueous" to 3)

// Header, Body, Text, Icons, Buttons, Menus
val darkThemes = listOf(
    Color(0xFF2B2D30),
    Color(0xFF26282E),
    Color(0xFF7A7E85),
    Color(0xFFBFC1C7),
    Color(0xFF1E1F22),
    Color(0xFF2B2D30)
)

val celesteThemes = listOf(
    Color(0xFF42033D),
    Color(0xFF3c3147),
    Color(0xFF854798),
    Color(0xFF7C238C),
    Color(0xFF7C72AD),
    Color(0xFF680E4B)
)
//var