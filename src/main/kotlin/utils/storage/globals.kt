package utils.storage

import utils.images.ImagePipeline
import java.awt.Dimension
import java.awt.image.BufferedImage

/**
 * All global variables are stored as LockType<T> to prevent possible race conditions
 * with a user changing settings during generation
 */
object Global {
    var loadedImageSize: LockType<Dimension?> = LockType(null)
    var loadedImage: LockType<BufferedImage?> = LockType(null)

    var outputLocation: String? = null

    // Square Gen Settings
    var squareRows: LockType<Int> = LockType(5)
    var squareColumns: LockType<Int> = LockType(5)

    val pipeline: ImagePipeline = ImagePipeline()
}