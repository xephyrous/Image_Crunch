package utils.storage

import androidx.compose.ui.graphics.Color
import java.awt.Dimension
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.awt.image.DataBufferInt
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.experimental.and
import kotlin.math.floor
import kotlin.math.min
import kotlin.reflect.KClass

enum class PipelineErrorType {
    CHAIN_ERROR
}

/**
 * An image byte mask.
 * ***Only for use in the Mask class!***
 */
typealias ImageMask = Array<Array<Byte>>

/**
 * Represents the types of image effectors
 */
enum class ImageEffectorType {
    /**
     * Provides an ImageData object, always the first effector in the chain
     */
    PROVIDER,
    /**
     * Applies a filter to the image, only changes individual pixel values
     */
    FILTER,

    /**
     * Applies an effect to the image, modifies any pixel's value
     */
    EFFECT,

    /**
     * Performs a manipulation of the image's pixels
     */
    MANIPULATION,

    /**
     * Adds a cut to the image
     */
    CUTTER
}

/**
 * Custom image holder for improved pixel data access and processing
 * @param image The base image to convert to data
 */
class ImageData(private val image: BufferedImage) {
    // TODO("Validate this actually works (Thanks Shit-GPT)")
    private val _data: ByteArray = (image.raster.dataBuffer as DataBufferInt).data.flatMap {
        listOf(
            (it shr 24).toByte(),
            (it shr 16).toByte(),
            (it shr 8).toByte(),
            it.toByte()
        )
    }.toByteArray()

    private lateinit var _view: ImageDataView

    val width: Int = image.width
    val height: Int = image.height

    /**
     * Converts coordinates on the stored image to the corresponding position in the [ByteArray]
     * @param x The x-coordinate of the pixel in the image.
     * @param y The y-coordinate of the pixel in the image,
     * @return The index of the pixel at the coordinates ([x], [y])
     */
    fun indexOf(x: Int, y: Int): Int {
        if (x !in 0..image.width || y !in 0..image.height) {
            throw IndexOutOfBoundsException("Invalid position in image!")
        }

        return (image.width * y + x)
    }

    /**
     * A helper function for [indexOf] to accept a Pair<Int, Int>
     * @param pos A position on the parent image to convert to an index
     * @return The index of the pixel at the coordinate [pos]
     */
    fun indexOf(pos: Pair<Int, Int>): Int {
        return indexOf(pos.first, pos.second)
    }

    /**
     * Returns the color at a specified index in [_data]
     * Automatically determines if the image has an alpha channel
     * @param index The index of the pixel to get the color of
     * @return The color of the pixel
     */
    fun colorAt(index: Int): Color {
        getImage().onSuccess {
            if (it.colorModel.hasAlpha()) {
                return rgba(index)
            }

            return rgb(index)
        }.onFailure {
            throw it
        }

        // Default to rgb
        return rgb(index)
    }

    /**
     * Returns the color of a given pixel in RGB.
     * @param index The index of the pixel in the image data ([_data])
     * @return The color (in RGB) of the pixel at the given index
     */
    fun rgb(index: Int): Color {
        return Color(
            red = _data[index].and(0xff.toByte()).toInt().shl(16),
            green = _data[index].and(0xff.toByte()).toInt().shl(8),
            blue = _data[index].and(0xff.toByte()).toInt(),
        )
    }

    /**
     * Returns the color of a given pixel in RGBA.
     * @param index The index of the pixel in the image data ([_data])
     * @return The color (in RGBA) of the pixel at the given index
     */
    fun rgba(index: Int): Color {
        return Color(
            red = _data[index].and(0xff.toByte()).toInt().shl(16),
            green = _data[index].and(0xff.toByte()).toInt().shl(8),
            blue = _data[index].and(0xff.toByte()).toInt(),
            alpha = _data[index].and(0xff.toByte()).toInt().shl(24)
        )
    }

    /**
     * Returns the image data ([_data])
     * @return The image data ([_data]) as a [BufferedImage]
     */
    fun getImage(): Result<BufferedImage> {
        return try {
            Result.success(ImageIO.read(ByteArrayInputStream(_data)))
        } catch (err: IOException) {
            Result.failure(IOException("Failed to read image from (possibly corrupt) data!"))
        }
    }

    /**
     * Returns the image data ([_data])
     * @return The image data ([_data]) as a [ByteArray]
     */
    fun data(): ByteArray {
        return _data
    }
}

/**
 * Represents an exposed view of an image
 * @param image A reference to the parent class's stored image
 */
abstract class ImageDataView(image: ImageData) {
    /**
     * Holds a reference to the parent image
     */
    val _image: ImageData = image

    /**
     * @return The value at a given coordinate
     */
    abstract operator fun get(pos: Pair<Int, Int>): Color?

    /**
     * Sets the value at a given coordinate
     */
    abstract operator fun set(pos: Pair<Int, Int>, value: Color)

    /**
     * Steps the view to its next position/orientation on the image
     */
    abstract fun next(): Result<Unit>

    /**
     * Sets the values of a range of coordinates
     * @param startIndex The starting position of the range
     * @param endIndex The ending position of the range
     * @param value The color to set the pixels to
     */
    operator fun set(startIndex: Pair<Int, Int>, endIndex: Pair<Int, Int>, value: Color) {
        (startIndex.second..endIndex.second).forEach { i ->
            (startIndex.first..endIndex.first).forEach { j ->
                set(Pair(i, j), value)
            }
        }
    }

    /**
     * Sets the color of any given coordinates
     * @param indexes The indexes to set the color at
     * @param value The color to set the pixels to
     */
    operator fun set(vararg indexes: Pair<Int, Int>, value: Color) {
        indexes.forEach { set(it, value) }
    }
}

/**
 * A rect of [_size] dimensions that moves over the image
 * @param image A reference to the parent image
 * @param snap Whether the rect should move by one pixel each step (`false`), or snap to the edge of the last rect (`true`).
 * (Defaults to `false`)
 */
class RectView(
    private val image: ImageData,
    size: Int, private val snap: Boolean = false
) : ImageDataView(image) {
    /**
     * The size of the rect (width, height)
     */
    private val _size: Pair<Int, Int> = Pair(3, 3)

    /**
     * The top-left corner of the current rect (x, y)
     */
    private val _pos: Pair<Int, Int> = Pair(0, 0)

    /**
     * Checks if a given position is within the current rect, and does not extend past the image's bounds
     * @param pos The coordinates to check
     * @return Whether the given position is withing the current rect & image bounds
     */
    private fun boundsCheck(pos: Pair<Int, Int>): Boolean {
        return pos.first in _pos.first..(_pos.first + _size.first).coerceIn(0.._image.width)
                && pos.second in _pos.second..(_pos.second + _size.second).coerceIn(0.._image.height)
    }

    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        if (!boundsCheck(pos)) {
            return null
        }

        return _image.colorAt(_image.indexOf(pos))
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        if (!boundsCheck(pos)) {
            throw IndexOutOfBoundsException("Invalid position in image!")
        }

        // image.data()[image.indexOf()]
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document & Finish RowView
 */
class RowView(image: ImageData) : ImageDataView(image) {
    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document & Finish ColumnView
 */
class ColumnView(image: ImageData) : ImageDataView(image) {
    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document & Finish CardinalView
 */
class CardinalView(image: ImageData) : ImageDataView(image) {
    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document & Finish DiagonalView
 */
class DiagonalView(image: ImageData) : ImageDataView(image) {
    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document & Finish PolarView
 */
class PolarView(image: ImageData) : ImageDataView(image) {
    /**
     * Override of [ImageDataView.get]
     */
    override fun get(pos: Pair<Int, Int>): Color? {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.set]
     */
    override fun set(pos: Pair<Int, Int>, value: Color) {
        TODO("Not yet implemented")
    }

    /**
     * Override of [ImageDataView.next]
     */
    override fun next(): Result<Unit> {
        TODO("Not yet implemented")
    }
}

/**
 * TODO : Document ImageEffector
 */
abstract class ImageEffector<TIn, TOut> {
    abstract val type: ImageEffectorType
    abstract val inputType: KClass<*>
    abstract val outputType: KClass<*>

    abstract fun apply(data: TIn): TOut

    protected fun chainIOCheck(next: ImageEffector<*, *>): Boolean {
        return this.inputType == next.inputType && this.outputType == next.outputType
    }
}

/**
 * Pure image provider class, non-abstract because it is only used as the starting effector
 */
class ImageProvider : ImageEffector<BufferedImage, ImageData>() {
    override val type: ImageEffectorType = ImageEffectorType.PROVIDER
    override val inputType: KClass<*> = Unit::class
    override val outputType: KClass<*> = Unit::class

    override fun apply(data: BufferedImage): ImageData {
        return ImageData(data)
    }
}

/**
 * Applies a filter to an image, modifying pixel values individually
 */
abstract class ImageFilter : ImageEffector<ImageData, Unit>() {
    override val type: ImageEffectorType = ImageEffectorType.FILTER
    override val inputType: KClass<*> = ImageData::class
    override val outputType: KClass<*> = Unit::class

    /**
     * Changes a pixel color by a given function
     */
    abstract fun pixelMod(pixelValue: Color): Color

    /**
     * Sequentially applies the pixel modification function over each pixel in the image
     * TODO : Parallel optimization with multithreading
     */
    override fun apply(data: ImageData) {
        for (i in 0..data.data().size) {
            data.getImage().onSuccess {
                if (it.colorModel?.hasAlpha() == true) {
                    pixelMod(data.rgba(i))
                }
            }

            pixelMod(data.rgb(i))
        }
    }
}

/**
 * TODO : Document ImageEffect
 */
abstract class ImageEffect : ImageEffector<ImageData, ImageData>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.EFFECT
        set(_) {}

}

/**
 * TODO : Document ImageManipulation
 */
abstract class ImageManipulation : ImageEffector<ImageData, ImageData>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.MANIPULATION
        set(_) {}

}

/**
 * TODO : Document ImageCutter
 */
abstract class ImageCutter : ImageEffector<ImageData, ArrayList<Mask>>() {
    override val inputType: KClass<*> = ImageData::class
    override val outputType: KClass<*> = Array<Mask>::class

    override var type: ImageEffectorType
        get() = ImageEffectorType.CUTTER
        set(_) {}

}

/**
 * Cuts an image into a grid of [gridX] x [gridY] tiles
 * @param gridX The number of columns to cut
 * @param gridY The number of rows to cut
 */
class GridCutter(private val gridX: Int, private val gridY: Int) : ImageCutter() {
    /**
     * Cuts the loaded image into a grid of [gridX] x [gridY] tiles
     * @param data The image to cut
     * @return The cut pieces of the image as an [ArrayList] of [Mask]s
     */
    override fun apply(data: ImageData): ArrayList<Mask> {
        val masks: ArrayList<Mask> = arrayListOf()

        val spacingX = floor(data.width.toDouble() / gridX).toInt()
        val spacingY = floor(data.height.toDouble() / gridY).toInt()

        for (y: Int in 0..data.width - spacingX step spacingX) {
            for (x: Int in 0..data.height - spacingY step spacingY) {
                masks.add(
                    Mask(
                        Dimension(min(spacingX, data.width - x), min(spacingY, data.height - y)),
                        PositionNode(x, y)
                    )
                )
            }
        }

        return masks
    }

}

class ImagePipelineError(message: String, code: PipelineErrorType) : DecoratedError("PIPELINE", message, code.ordinal)