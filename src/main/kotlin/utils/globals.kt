package utils

import androidx.compose.ui.graphics.Color
import java.awt.Dimension
import java.awt.image.BufferedImage

var loadedImageSize: Dimension = Dimension()
var selectedImage: BufferedImage? = null
var overlayImage: BufferedImage? = null
var compactExport: Boolean = true

/**
 * Export value of Generator Type mapping
 */
val generatorTypeMap = mapOf("Square Generator" to 0)
var generatorType: Int = 0

// Square Gen Settings
var squareRows: Int = 15
var squareColumns: Int = 15