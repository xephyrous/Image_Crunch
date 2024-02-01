package utils

import java.awt.BasicStroke
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

// Copies image and displays a mask, shoutout to O(nxm)
fun createNodeMask(
    input: BufferedImage,
    nodes: ArrayList<PositionNode>,
    nodeColor: Color = Color.BLACK,
    nodeSize: Int = 3,
): BufferedImage {
    // Create Output
    val img = BufferedImage(loadedImageSize.width, loadedImageSize.height, BufferedImage.TYPE_INT_ARGB)
    val gpx: Graphics2D = img.graphics as Graphics2D

    gpx.color = nodeColor
    gpx.stroke = BasicStroke(5f)

    for(node in nodes) {
        gpx.fillOval(node.second, node.first, nodeSize, nodeSize)
    }

    return img
}