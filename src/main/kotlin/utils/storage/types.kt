package utils.storage

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
 *
 * @property NONE No set generator type, similar to a null, should be used as a fallback value
 * @property SQUARE Square grid of pieces
 * @property SQUARE_NOISE Square grid of pieces with noisy edges
 */
enum class GeneratorType {
    NONE,
    SQUARE,
    SQUARE_NOISE
}

/**
 * A masked region of an image with its top-left coordinate
 *
 * @param size The size of the mask
 *
 * @property bits The array of bits in the mask
 * @property position The position of the top-left corner of the mask
 */
class Mask(val size: Dimension) {
    var bits: ImageMask = Array(size.height) {
        Array(size.width) { 0 }
    }
    var position: PositionNode = PositionNode(-1, -1)

    /**
     * Used for debugging, prints the mask array showing only 1s
     */
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
 *
 * @param T The type of the data being stored
 * @param lockVal The data to be stored, of type [T]
 *
 * @property value The current value of the variable
 * @property locked If the variable is locked
 * @property holdVal Any queued value
 */
class LockType<T>(lockVal: T) {
    private var value: T = lockVal
    private var locked: Boolean = false
    private var holdVal: T? = null

    /**
     * Returns if the value is locked
     */
    fun locked() : Boolean { return locked }

    /**
     * Locks the value, it cannot be modified
     */
    fun lock() { locked = true }

    /**
     * Unlocks the property, it can be modified
     */
    fun unlock() { locked = false }

    /**
     * Returns the value of the data, updates the data if there is a value queued
     */
    fun value() : T {
        if(holdVal != null && !locked) {
            value = holdVal!!
            holdVal = null
        }

        return value
    }

    /**
     * Sets the value of the data, if locked it queues the value in [holdVal]
     */
    fun set(newVal: T) {
        if(locked) {
            holdVal = newVal
            return
        }

        value = newVal
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
 *
 * @property masks An array of all masks
 */
class ImageMaskArray() {
    var masks = ArrayList<Mask>()


    /**
     * Adds a mask to the array
     */
    fun add(m: Mask) { masks.add(m) }

    /**
     * Removes a mask from the array by
     */
    fun remove(m: Mask) { masks.remove(m) }

    /**
     * Removes a mask from the array at a given index
     */
    fun remove(idx: Int) { masks.removeAt(idx) }

    /**
     * Removes the last mask in the array
     */
    fun removeLast() { masks.remove(masks.last()) }

    /**
     * Removes the first mask in the array
     */
    fun removeFirst() { masks.remove(masks[0]) }

    /**
     * Returns the mask at a given index
     */
    fun get(idx: Int) : Mask { return masks[idx] }

    /**
     * Returns the last mask in the array
     */
    fun last() : Mask { return masks.last() }

    /**
     * Returns the first mask in the array
     */
    fun first() : Mask { return masks[0] }
}