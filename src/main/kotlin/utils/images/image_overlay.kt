package utils.images

import utils.storage.PositionNode
import utils.storage.Global.loadedImageSize
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

/**
 * Generates an image overlay of dots representing the generated nodes
 *
 * @param nodes The nodes to display
 * @param nodeColor The color of the nodes
 * @param nodeSize The diameter of each node
 *
 * @return An image overlay of the nodes
 */
fun createNodeMask(
    nodes: ArrayList<PositionNode>,
    nodeColor: Color = Color.BLACK,
    nodeSize: Int = 10,
): BufferedImage {
    // Create Output
    val img = BufferedImage(loadedImageSize.value!!.width, loadedImageSize.value!!.height, BufferedImage.TYPE_INT_ARGB)
    val gpx: Graphics2D = img.graphics as Graphics2D

    gpx.color = nodeColor
    gpx.stroke = BasicStroke(5f)

    for(node in nodes) {
        gpx.fillOval(node.first, node.second, nodeSize, nodeSize)
    }
    return img
}

/*
fun createCutMask() {

}
*/