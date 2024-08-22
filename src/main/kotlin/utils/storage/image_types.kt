package utils.storage

import androidx.compose.ui.graphics.Color
import java.awt.image.BufferedImage
import java.awt.image.DataBufferByte
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.experimental.and
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
    private val _data: ByteArray = (image.raster.dataBuffer as DataBufferByte).data
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
     *
     *
     * @param The
     *
     * @return
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
     */
    operator fun set(startIndex: Pair<Int, Int>, endIndex: Pair<Int, Int>, value: Color) {
        (startIndex.second..endIndex.second).forEach { i ->
            (startIndex.first..endIndex.first).forEach { j ->
                set(Pair(i, j), value)
            }
        }
    }

    /**
     * Sets the values of any given coordinates
     */
    operator fun set(vararg indexes: Pair<Int, Int>, value: Color) {
        indexes.forEach { set(it, value) }
    }
}

/**
 * A box of [_size] width and height that moves across the image
 * @param image A reference to the parent image
 * @param snap Whether the box should move by one pixel each step (`false`), or snap to the edge of the last box (`true`).
 * (Defaults to `false`)
 */
class BoxView(image: ImageData, size: Int, snap: Boolean = false) : ImageDataView(image) {
    private val _size = size

    /**
     * The top-left corner of the current box
     */
    private val _pos: Pair<Int, Int> = Pair(0, 0)

    /**
     * Checks if a given position is within the current box, and does not extend past the image's bounds
     * @return Whether the given position is withing the current box & image bounds
     */
    private fun boundsCheck(pos: Pair<Int, Int>): Boolean {
        return pos.first in _pos.first..(_pos.first + _size).coerceIn(0.._image.width)
                && pos.second in _pos.second..(_pos.second + _size).coerceIn(0.._image.height)
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
abstract class ImageEffect : ImageEffector<ImageData, Unit>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.EFFECT
        set(_) {}

}

/**
 * TODO : Document ImageManipulation
 */
abstract class ImageManipulation : ImageEffector<ImageData, Mask>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.MANIPULATION
        set(_) {}

}

/**
 * TODO : Document ImageCutter
 */
abstract class ImageCutter : ImageEffector<ImageData, Array<Mask>>() {
    override var type: ImageEffectorType
        get() = ImageEffectorType.CUTTER
        set(_) {}

}

class ImagePipelineError(message: String, code: PipelineErrorType) : DecoratedError("PIPELINE", message, code.ordinal)