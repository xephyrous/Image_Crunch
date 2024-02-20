package utils.storage

import java.awt.Dimension
import java.io.FileOutputStream

/**
 * All global variables are stored as LockType<T> to prevent possible race conditions
 * with a user changing settings during generation
 */

/**
 * Loaded image details
 */
var loadedImageSize: LockType<Dimension> = LockType(Dimension())
var compactExport: Boolean = true

var outputLocation: FileOutputStream? = null

var generatorType: LockType<GeneratorType> = LockType(GeneratorType.NONE)

// Square Gen Settings
var squareRows: LockType<Int> = LockType(15)
var squareColumns: LockType<Int> = LockType(15)