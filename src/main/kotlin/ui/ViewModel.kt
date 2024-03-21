package ui

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import utils.storage.aqueousThemes
import java.awt.image.BufferedImage

/**
 * ViewModel implementation for the application
 * Holds shared variables and passes them through scopes
 */
class ViewModel {
    var themeColor by mutableStateOf(aqueousThemes)

    // Image Displays
    var displayedImage by mutableStateOf<BufferedImage?>(null)
    var displayedNodes by mutableStateOf<BufferedImage?>(null)

    var imageInputStream by mutableStateOf<ImageBitmap?>(null)
    var nodeInputStream by mutableStateOf<ImageBitmap?>(null)

    var imageBitmapPainter by mutableStateOf<Painter?>(null)
    var nodeBitmapPainter by mutableStateOf<Painter?>(null)

    // The good shi
    var settingsPage by mutableStateOf(0)
    var settingsLines by mutableStateOf( listOf(3, 4, 1) )

    var configGenerator by mutableStateOf(false)
    var configMasks by mutableStateOf(false)
    var configSlices by mutableStateOf(false)

    var selectedGenerator by mutableStateOf(1)

    var menuPage by mutableStateOf(0)
    var menuLines by mutableStateOf( listOf(3, 3, 4) )

    var screenWidth by mutableStateOf(1200.dp)
    var screenHeight by mutableStateOf(800.dp)

    var imageModifier by mutableStateOf(
        Modifier.size(width = (screenWidth/2)-10.dp, height = screenHeight-230.dp).offset(5.dp, 5.dp)
    )

    // Display Settings
    var imageDisplay by mutableStateOf(false)
    var nodeDisplay by mutableStateOf(true)

    // Card Animations
    var menuCardState by mutableStateOf(false) 
    var settingsCardState by mutableStateOf(false)
}