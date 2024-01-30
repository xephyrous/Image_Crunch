package utils

import kotlin.math.ceil

/**
 *
 */
fun squareCutGenerator(
    nodes: ArrayList<PositionNode>,
    rows: Int,
    columns: Int
) : Mask {
    val cutMask: Mask = Mask(loadedImageSize)
    var backStep: Int

    for(pos in 0 until nodes.size) {
        //Step from node to edge/prev. node horizontally
        if(nodes[pos].first != 0) {
            backStep =  if (nodes[pos].first == ceil(loadedImageSize.width / columns.toDouble()).toInt())
                            nodes[pos].first else nodes[pos - 1].first

            for(xPos in (nodes[pos].first - backStep)
                        .coerceAtLeast(0) .. nodes[pos].first) {
                cutMask.bits[nodes[pos].second][xPos] = 1
            }
        }

        //Step from node to edge/prev. node vertically
        if(nodes[pos].second != 0) {
            backStep =  if (nodes[pos].second == ceil(loadedImageSize.height / rows.toDouble()).toInt())
                nodes[pos].second else nodes[pos - 1].second

            for(yPos in (nodes[pos].second - backStep).coerceAtLeast(0) .. nodes[pos].second) {
                cutMask.bits[yPos][nodes[pos].first] = 1
            }
        }
    }

    //DEBUG PRINTING
    var nodePos = 0;
    for(x in 0 until cutMask.bits.size) {
        for(y in 0 until cutMask.bits[0].size) {
            if(cutMask.bits[x][y] == 1.toByte()) { print("1"); continue }
            print("0")
        }
        println()
    }

    return cutMask
}