package utils

/**
 * @param cutImage Cut image mask
 * @return List of masked pieces of the image
 */
fun squareMaskGenerator(
    cutImage: ImageMask,
    nodes: ArrayList<PositionNode>
) : ArrayList<Mask> {
    val masks: ArrayList<Mask> = ArrayList();

    for(pos in 0 until nodes.size) {
        if(nodes[pos])
    }

    return masks;
}