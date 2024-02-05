package utils.images

import utils.storage.Mask
import utils.storage.PositionNode
import utils.storage.loadedImageSize
import kotlin.math.ceil

/**
 *
 */
fun squareCutGenerator(
    nodes: ArrayList<PositionNode>,
    rows: Int,
    columns: Int
) : Mask {
    val cutMask = Mask(loadedImageSize.value())
    var backStep: Int

    for(pos in 0 until nodes.size) {
        //Step from node to edge/prev. node horizontally
        if(nodes[pos].first != 0) {
            backStep =  if (nodes[pos].first == ceil(loadedImageSize.value().width / columns.toDouble()).toInt())
                            nodes[pos].first else nodes[pos - 1].first

            for(xPos in (nodes[pos].first - backStep)
                        .coerceAtLeast(0) .. nodes[pos].first) {
                cutMask.bits[nodes[pos].second][xPos] = 1
            }
        }

        //Step from node to edge/prev. node vertically
        if(nodes[pos].second != 0) {
            backStep =  if (nodes[pos].second == ceil(loadedImageSize.value().height / rows.toDouble()).toInt())
                nodes[pos].second else nodes[pos - 1].second

            for(yPos in (nodes[pos].second - backStep).coerceAtLeast(0) .. nodes[pos].second) {
                cutMask.bits[yPos][nodes[pos].first] = 1
            }
        }
    }

    return cutMask
}