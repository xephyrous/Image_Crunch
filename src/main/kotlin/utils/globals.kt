package utils

import androidx.compose.ui.graphics.Color
import java.awt.Dimension
import java.io.File

var loadedImageSize: Dimension = Dimension()
var selectedImage: File? = null
var compactExport: Boolean = true

val generatorTypeMap = mapOf("Square Generator" to 0)
var generatorType: Int = 0

// Square Gen Settings
var squareRows: Int = 15
var squareColumns: Int = 15

// Header, Body, Text, Icons, Buttons, Menus
val darkThemes = listOf(
    Color(0xFF2B2D30),
    Color(0xFF26282E),
    Color(0xFFBFC1C7),
    Color(0xFF7A7E85),
    Color(0xFF1E1F22),
    Color(0xFF2B2D30)
)

val lightThemes = listOf(
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF),
    Color(0xFFFFFB00),
    Color(0xFF00FF00),
    Color(0xFFFFFFFF),
    Color(0xFFFFFFFF)
)

// PLEASE MAKE THESE BETTER COLORS I CANT
val celesteThemes = listOf(
    Color(0xFF42033D),
    Color(0xFF3c3147),
    Color(0xFF854798),
    Color(0xFF7C238C),
    Color(0xFF7C72AD),
    Color(0xFF680E4B)
)

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