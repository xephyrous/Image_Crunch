package utils.storage

import java.awt.Dimension

//An image as a byte mask
typealias ImageMask = Array<Array<Byte>>

//A single position on an image
typealias PositionNode = Pair<Int, Int>

//List of node generation functions
enum class GeneratorType {
    NONE,
    SQUARE
}

//A masked region of an image with its top-left coordinate
class Mask(val size: Dimension) {
    var bits: Array<Array<Byte>>
    var position: PositionNode = PositionNode(-1, -1)

    init {
        bits = Array(size.height) {
            Array(size.width) { 0 }
        }
    }

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