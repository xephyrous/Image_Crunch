package utils.storage

import androidx.compose.ui.graphics.Color
import java.awt.Dimension

/**
 * An image as a byte mask
 */
typealias ImageMask = Array<Array<Byte>>

/**
 * A single position on an image
 */
typealias PositionNode = Pair<Int, Int>

/**
 * List of node generation functions
 */
enum class GeneratorType {
    NONE,
    SQUARE,
    SQUARE_NOISE
}

/**
 * A masked region of an image with its top-left coordinate
 */
class Mask(val size: Dimension) {
    var bits: ImageMask = Array(size.height) {
        Array(size.width) { 0 }
    }
    var position: PositionNode = PositionNode(-1, -1)

    fun print() {
        for(y in bits) {
            for(x in y) {
                print(1)
            }
            println()
        }
    }
}

/**
 * Protects a variable from race conditions by "locking" the value
 * It will also hold any values attempted to be set while locked and update after it is unlocked
 */
class LockType<T>(lockVal: T) {
    private var value: T = lockVal
    private var locked: Boolean = false
    private var holdVal: T? = null

    fun locked() : Boolean { return locked }
    fun lock() { locked = true }
    fun unlock() { locked = false }

    fun value() : T {
        if(holdVal != null && !locked) {
            value = holdVal!!
            holdVal = null
        }

        return value
    }
    fun set(newVal: T) {
        if(locked) {
            holdVal = newVal
            return
        }

        value = newVal
    }
}

// Add a new theme object: idk what im doing
class AppTheme(
    name: String,
//    header: Color,
//    text1: Color,
//    text2: Color,
//    textGrey: Color,
//    icon: Color,
//    bgGrad1: Color,
//    bgGrad2: Color,
//    bgGrad3: Color,
//    cGrad1: Color,
//    cGrad2: Color,
//    button: Color,
//    fab: Color,
//    tFOn: Color,
//    tFOff: Color,
//    tFbg: Color,
//    tFCursor: Color,
//    tFFocus: Color,
//    tFUFocus: Color,
//    border: Color,
) {
    var themeName = name
    val colors = arrayOfNulls<Color>(19)

    fun set(position: Int, color: Color) { colors[position] = color }

    fun changeName(name: String) { themeName = name }

    fun getColors(): List<Color> {
        return if (null in colors) emptyList() else (colors.toList() as List<Color>)
    }
}

/**
 * Converts a string to a given type based on a detected inline type
 */
inline fun <reified T> String.convert(): T {
    return when(T::class){
        Double::class -> toDouble()
        Int::class -> toInt()
        Boolean::class -> toBoolean()
        else -> error("Converter unavailable for ${T::class}")
    } as T
}

/**
 * Converts a string to a given type based on a type parameter
 */
inline fun <reified T> String.convertT(typeParam: T): T {
    return when(typeParam!!::class){
        Double::class -> toDouble()
        Int::class -> toInt()
        Boolean::class -> toBoolean()
        else -> error("Converter unavailable for ${T::class}")
    } as T
}

/**
 * TODO : Create for parsing .meow files (lord please rename this atrocious file extension 🙏)
 * Holds a variable name and type along with casting functions (I think (Forgot why I made this 😁))
 */
class TypedValue(name: String, type: String) {

}

/**
 * Represents an array of masks with modification functions
 * Contains Max and Min values along with dimensions for the array
 */
class ImageMaskArray() {
    var masks = ArrayList<Mask>()

    var max = Pair(0, 0)
    var min = Pair(0, 0)

    var maskSize = Dimension(0, 0)

    fun add(m: Mask) { masks.add(m) }
    fun remove(m: Mask) { masks.remove(m) }
    fun remove(idx: Int) { masks.removeAt(idx) }
    fun removeLast() { masks.remove(masks.last()) }
    fun removeFirst() { masks.remove(masks[0]) }

    fun get(idx: Int) : Mask { return masks[idx] }
    fun last() : Mask { return masks.last() }
    fun first() : Mask { return masks[0] }
}