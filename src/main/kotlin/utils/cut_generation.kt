package utils

/**
 *
 */
fun squareCutGenerator(
    nodes: ArrayList<PositionNode>,
    rows: Int,
    columns: Int
) : Mask {
    val cutMask: Mask = Mask(loadedImageSize)
    val hSpace = (loadedImageSize.width / rows)
    val vSpace = (loadedImageSize.height / columns)

    for(pos in 0 until nodes.size) {
        if(nodes[pos].first != 0) {
            //Step from node to edge/prev. node
            for(xPos in nodes[pos].first - hSpace .. nodes[pos].first) {
                println("[${xPos},${nodes[pos].second}]")
                cutMask.bits[nodes[pos].second][xPos] = 1
            }
        }

        if(nodes[pos].second != 0) {
            //Up
        }
    }

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