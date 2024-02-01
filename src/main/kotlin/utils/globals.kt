package utils

import androidx.compose.ui.graphics.Color
import java.awt.Dimension
import java.io.File

var loadedImageSize: Dimension = Dimension()
var selectedImage: File? = null
var compactExport: Boolean = true

/**
 * Export value of Generator Type mapping
 */
val generatorTypeMap = mapOf("Square Generator" to 0)
var generatorType: Int = 0

// Square Gen Settings
var squareRows: Int = 15
var squareColumns: Int = 15