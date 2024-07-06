package utils.storage

import java.awt.Dimension
import java.awt.image.BufferedImage

/**
 * All global variables are stored as LockType<T> to prevent possible race conditions
 * with a user changing settings during generation
 */

/**
 * Loaded image details
 */

object Global {
    var loadedImageSize: LockType<Dimension?> = LockType(null)
    var loadedImage: LockType<BufferedImage?> = LockType(null)

    var outputLocation: String? = null

    var generatorType: LockType<GeneratorType> = LockType(GeneratorType.SQUARE)
    var nodes: LockType<ArrayList<PositionNode>?> = LockType(null)
    var mask: LockType<Mask?> = LockType(null)
    var slices: LockType<ArrayList<Mask>?> = LockType(null)

    // Square Gen Settings
    var squareRows: LockType<Int> = LockType(5)
    var squareColumns: LockType<Int> = LockType(5)

    // Cut Noise
    var cutNoise: LockType<Float> = LockType(0.0F)
}

