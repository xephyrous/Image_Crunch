package utils

import java.awt.Color
import java.awt.image.BufferedImage

// Copies image and displays a mask, shoutout to O(nxm)
fun displayNodeMask(input: BufferedImage, nodes: ArrayList<PositionNode>): BufferedImage {
    // Create Output
    val img = BufferedImage(loadedImageSize.width, loadedImageSize.height, BufferedImage.TYPE_INT_ARGB)

    try {
        for (node in nodes) {
            try {
                img.setRGB(node.second, node.first, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second+1, node.first, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second, node.first+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second+1, node.first+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second+1, node.first-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second-1, node.first+1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second-1, node.first, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second, node.first-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
            try {
                img.setRGB(node.second-1, node.first-1, Color(0,0,0).rgb)
            } catch (_: Exception) {}
        }
    } catch(e: Exception) {
        println(e.stackTrace)
    }

    return img
}