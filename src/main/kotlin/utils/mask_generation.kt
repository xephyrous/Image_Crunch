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
        //Iterate through node positions
        for(y in 0..nodes[pos].second) {
            for(x in 0..nodes[pos].first) {

            }
        }

    }

    return masks;
}