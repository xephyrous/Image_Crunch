package utils

import java.awt.Dimension

//An image as a byte mask
typealias ImageMask = Array<Array<Byte>>

//A single position on an image
typealias PositionNode = Pair<Int, Int>

//A masked region of an image with its top-left coordinate
class Mask(size: Dimension) {
    var bits: Array<Array<Byte>>;
    var position: PositionNode = PositionNode(-1, -1)
    final val size = size;

    init {
        bits = Array(size.height) {
            Array(size.width) { 0 }
        }
    }
}