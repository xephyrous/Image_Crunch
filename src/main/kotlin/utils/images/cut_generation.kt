package utils.images

import utils.storage.*
import kotlin.math.ceil

/**
 * Cuts an image mask into square pieces with edge noise
 *
 * @param nodes Used for positioning the cut generation
 *
 * @return A mask of bits representing a cut line (1) or nothing (0)
 */
fun squareNoiseCutGenerator(
    nodes: ArrayList<PositionNode>,
) : Mask {
    val cutMask = Mask(loadedImageSize.value()!!)
    var backStep: Int

    for(pos in 0 until nodes.size) {
        //Step from node to edge/prev. node horizontally
        if(nodes[pos].first != 0) {
            backStep =  if (nodes[pos].first == ceil(loadedImageSize.value()!!.width / squareColumns.value().toDouble()).toInt())
                            nodes[pos].first else nodes[pos - 1].first

            for(xPos in (nodes[pos].first - backStep)
                        .coerceAtLeast(0) .. nodes[pos].first) {
                cutMask.bits[nodes[pos].second][xPos] = 1
            }
        }

        //Step from node to edge/prev. node vertically
        if(nodes[pos].second != 0) {
            backStep =  if (nodes[pos].second == ceil(loadedImageSize.value()!!.height / squareRows.value().toDouble()).toInt())
                nodes[pos].second else nodes[pos - 1].second

            for(yPos in (nodes[pos].second - backStep).coerceAtLeast(0) .. nodes[pos].second) {
                cutMask.bits[yPos][nodes[pos].first] = 1
            }
        }
    }

    return cutMask
}