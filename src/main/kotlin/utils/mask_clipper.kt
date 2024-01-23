package utils

import java.awt.Dimension
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

// TODO: Figure out better input method (needs to return as one or smth)

/**
 * maskToImage
 *
 * Converts a given image to smaller image based on a given mask
 *
 * @param [input] The given image to be cut.
 * @param [mask] The mask to be applied to the given image.
 * @param [startCoords] The Coordinates to place the mask at | Given as y, x
 * @param [name] Output file name.
 */
fun maskToImage(input: BufferedImage, mask: Array<Array<Int>>, startCoords: Array<Int>, name: String) {
    // Create Output
    val size = Dimension(mask[1].size, mask.size)
    val img = BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_RGB)

    // idk why it wants me to use `until` rather than `..<` but it yelled at me so
    for (x in 0 until size.width) {
        for (y in 0 until size.height) {
            img.setRGB(x, y, input.getRGB(x+startCoords[0], y+startCoords[0]))
        }
    }

    ImageIO.write(img, "JPG", File("$name.jpg"))
}