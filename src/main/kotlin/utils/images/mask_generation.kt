package utils.images

import utils.storage.ImageMask
import utils.storage.Mask
import utils.storage.PositionNode
import utils.storage.loadedImageSize
import java.awt.Dimension

/**
 * @return List of masked pieces of the image
 */
fun squareMaskGenerator(
    nodes: ArrayList<PositionNode>,
    rows: Int,
    columns: Int
) : ArrayList<Mask> {
    val masks: ArrayList<Mask> = ArrayList()

    var currRow = 0
    var currColumn = 0

    var topNode: PositionNode
    var bottomNode: PositionNode

    var addMask: Mask
    var tempBits: ImageMask

    for(i in 0 .. nodes.size) {
        if(currRow == rows) { break }

        // Advance Column
        if(currColumn == columns) {
            currRow++
            currColumn = 0
            continue
        }

        println("Column x Row - [${currColumn + 1}, ${currRow + 1}]")
        topNode = nodes[(currRow * (columns + 1)) + (currColumn)]
        println("Top Node - [${topNode.first}, ${topNode.second}]")
        bottomNode = nodes[((currRow + 1) * (columns + 1)) + (currColumn + 1)]
        println("Bottom Node - [${bottomNode.first}, ${bottomNode.second}]\n")

        addMask = Mask(Dimension(bottomNode.first - topNode.first, bottomNode.second - topNode.second))

        tempBits = Array(addMask.size.height) {
            Array(addMask.size.width) { 1 }
        }
        addMask.bits = tempBits

        masks.add(addMask)
        currColumn++
    }

    return masks
}