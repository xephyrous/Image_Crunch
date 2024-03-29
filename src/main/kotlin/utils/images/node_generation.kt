package utils.images

import utils.storage.PositionNode
import utils.storage.loadedImageSize
import kotlin.math.ceil

// Node Generation Functions must be (list: ParameterList) -> ArrayList<PositionNode>

/**
 * Generates nodes on an image in a square grid
 *
 * @param rows The number of rows
 * @param columns The number of columns
 *
 * @return List of PositionNodes on the image
 *
 * TODO Add fix for las row/column jamming due to ceil()
 */
fun squareNodeGenerator(
    rows: Int, columns: Int
) : ArrayList<PositionNode> {
    val nodeList: ArrayList<PositionNode> = ArrayList()

    val xSpacing: Int  = ceil(loadedImageSize.value()!!.width / columns.toDouble()).toInt()
    val ySpacing: Int  = ceil(loadedImageSize.value()!!.height / rows.toDouble()).toInt()

    for(y in 0 .. rows) {
        for(x in 0 .. columns) {
            nodeList.add(
                PositionNode(
                    (x * xSpacing).coerceAtMost(loadedImageSize.value()!!.width - 1),
                    (y * ySpacing).coerceAtMost(loadedImageSize.value()!!.height - 1)
                )
            )
        }
    }

    return nodeList
}
