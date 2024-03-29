package utils.images

import utils.storage.Mask
import utils.storage.outputLocation
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

/**
 * Returns the dimensions of an image
 *
 * @param bufferedImage The image to get the dimensions of
 *
 * @return The size of the image
 */
fun getDim(bufferedImage: BufferedImage): Dimension {
    return Dimension(bufferedImage.width, bufferedImage.height)
}

/**
 * Converts an image file to a BufferedImage object
 *
 * @param input The file to convert
 *
 * @return The image as a BufferedImage
 */
fun fileToBufferedImage(input: File?): BufferedImage {
    val inp = ImageIO.read(input)

    val output = BufferedImage(inp.width, inp.height, BufferedImage.TYPE_INT_ARGB)

    for (x in 0 until inp.width) {
        for (y in 0 until inp.height) {
            output.setRGB(x, y, inp.getRGB(x, y))
        }
    }

    return output
}

/**
 * Converts a BufferedImage to a ByteArrayInputStream
 *
 * @param input The BufferedImage object to convert
 *
 * @return The BufferedImage as a ByteArrayInputStream
 */
fun bufferedImageToOutputStream(input: BufferedImage): ByteArrayInputStream {
    val outputStream = ByteArrayOutputStream()
    ImageIO.write(input, "PNG", outputStream)
    return ByteArrayInputStream(outputStream.toByteArray())
}

/**
 * Converts an image to smaller image based on a given mask
 *
 * @param input The given image to be cut.
 * @param mask The mask to be applied to the given image.
 * @param name Output file name.
 */
fun maskToImage(input: BufferedImage, mask: Mask, name: String) {
    // Create Output
    val size = Dimension(mask.bits[1].size, mask.bits.size)
    val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB)

    for (x in 0 until size.width) {
        for (y in 0 until size.height) {
            if (mask.bits[y][x] == 1.toByte()) {
                img.setRGB(x, y, input.getRGB(x+mask.position.first, y+mask.position.second))
            }
        }
    }

    ImageIO.write(img, "PNG", File("${outputLocation}\\$name.png"))
}
