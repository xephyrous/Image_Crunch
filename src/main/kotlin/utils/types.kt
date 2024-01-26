package utils

//An image as a byte mask
typealias ImageMask = Array<Array<Byte>>

//A single position on an image
typealias PositionNode = Pair<Int, Int>

//Repre
class Mask {
    lateinit var bits: Array<Array<Int>>;
    lateinit var position: PositionNode;
}