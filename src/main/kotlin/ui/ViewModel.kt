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
 *
 * @property themeColor The currently selected theme for the application (Default is Aqueous)
 * @property displayedImage The currently displayed image
 * @property displayedNodes The graphics for the generated nodes
 * @property imageInputStream An input stream for writing to the [displayedImage]
 * @property nodeInputStream An input stream for writing to the [displayedNodes]
 * @property imageBitmapPainter A bitmap painter for updating the graphics of the [displayedImage]
 * @property nodeBitmapPainter A bitmap painter for updating the graphics of the [displayedNodes]
 * @property settingsPage The current page of the settings menu
 * @property settingsLines Offsets for the settings menu contents
 * @property configGenerator If the configuration general settings card is displayed
 * @property configMasks If the configuration menu mask settings card is displayed
 * @property configSlices If the configuration menu slices settings card is displayed
 * @property selectedGenerator The currently selected generator for the image pipeline
 * @property menuPage The current page of the main menu
 * @property menuLines Offsets for the main menu contents
 * @property screenWidth The width of the target screen
 * @property screenHeight The height of the target screen
 * @property imageModifier Controls the modifiers of the displayed image and nodes
 * @property imageDisplay If the image is displayed
 * @property nodeDisplay If the nodes are displayed
 * @property menuCardState If the menu card animation is running
 * @property settingsCardState If the settings card animation is running
 */
class ViewModel {
    // Application theme
    var themeColor by mutableStateOf(aqueousThemes)

    // Image Displays
    var displayedImage by mutableStateOf<BufferedImage?>(null)
    var displayedNodes by mutableStateOf<BufferedImage?>(null)

    var imageInputStream by mutableStateOf<ImageBitmap?>(null)
    var nodeInputStream by mutableStateOf<ImageBitmap?>(null)

    var imageBitmapPainter by mutableStateOf<Painter?>(null)
    var nodeBitmapPainter by mutableStateOf<Painter?>(null)

    // Internal variables
    var settingsPage by mutableStateOf(0)
    var settingsLines by mutableStateOf( listOf(3, 4, 1) )

    var configGenerator by mutableStateOf(false)
    var configMasks by mutableStateOf(false)
    var configSlices by mutableStateOf(false)

    var selectedGenerator by mutableStateOf(1)

    var menuPage by mutableStateOf(0)
    var menuLines by mutableStateOf( listOf(3, 5) )

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