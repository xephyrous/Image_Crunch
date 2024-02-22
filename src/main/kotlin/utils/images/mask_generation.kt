package utils.images

import utils.storage.ImageMask
import utils.storage.Mask
import utils.storage.PositionNode
import utils.storage.loadedImageSize
import java.awt.Dimension

/**
 * @param cutImage Cut image mask
 * @return List of masked pieces of the image
 */
fun squareMaskGenerator(
    cutImage: Mask,
    nodes: ArrayList<PositionNode>,
    rows: Int,
    columns: Int
) : ArrayList<Mask> {
    val masks: ArrayList<Mask> = ArrayList()

    var currRow = 0
    var currColumn = 0

    var cornerNode: PositionNode
    var posX = 0
    var posY = 0

    var addMask: Mask
    var tempBits: ImageMask

    for(node in nodes) {
        if(currColumn == rows + 2) {
            currRow++
            currColumn = 0
            continue
        }

        //Reach to bottom of image
        if(currRow == rows + 1) {
            posY = loadedImageSize.value()!!.height - 1
        } else {
            //Get node 1 row ahead and 1 row ahead (SouthEast of the current node)
            posY = nodes[((currRow + 1) * (rows + 2)) + currColumn + 1].second
        }

        //Reach to side of image
        if(currColumn == columns + 1) {
            posX = loadedImageSize.value()!!.width - 1
        } else {
            //Get node 1 row ahead and 1 row ahead (SouthEast of the current node)
            posX = nodes[((currRow + 1) * (rows + 2)) + currColumn + 1].first
        }

        //Set corner position and initialize mask
        cornerNode = PositionNode(posX, posY)
        addMask = Mask(
            Dimension(cornerNode.first - node.first, cornerNode.second - node.second)
        )

        //Create mask
        tempBits = Array(addMask.size.height) {
            Array(addMask.size.width) { 1 }
        }

        addMask.print()

        masks.add(addMask)
        currColumn++
    }

    return masks
}