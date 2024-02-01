package utils

import java.awt.Color
import java.awt.image.BufferedImage

// Copies image and displays a mask, shoutout to O(nxm)
fun displayNodeMask(nodes: ArrayList<PositionNode>): BufferedImage {
    // Create Output
    val img = BufferedImage(loadedImageSize.width, loadedImageSize.height, BufferedImage.TYPE_INT_ARGB)

    try {
        for (node in nodes) {
            try {
                img.setRGB(node.first, node.second, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first+1, node.second, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first, node.second+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first+1, node.second+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first+1, node.second-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first-1, node.second+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first-1, node.second, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first, node.second-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.first-1, node.second-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
        }
    } catch(e: Exception) {
        println(e.stackTrace)
    }

    return img
}