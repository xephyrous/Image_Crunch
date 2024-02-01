package utils

import java.awt.Dimension

var loadedImageSize: Dimension = Dimension()
var compactExport: Boolean = true

/**
 * Export value of Generator Type mapping
 */
val generatorTypeMap = mapOf("Square Generator" to 0)
var generatorType: Int = 0

// Square Gen Settings
var squareRows: Int = 15
var squareColumns: Int = 15