package ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import utils.storage.compactExport
import utils.storage.darkThemes
import utils.storage.generatorType
import java.awt.image.BufferedImage

/**
 * ViewModel implementation for the application
 * Holds shared variables and passes them through scopes
 */
class ViewModel {
    var themeColor by mutableStateOf(darkThemes)

    // Image Displays
    var displayedImage by mutableStateOf<BufferedImage?>(null)
    var displayedNodes by mutableStateOf<BufferedImage?>(null)

    // Settings
    var settingsLines by mutableStateOf(3)
    var genType by mutableStateOf(generatorType)
    var menuLines by mutableStateOf(2)
    var compactExportToggle by mutableStateOf(compactExport)

    // The good shi
    var settingsMain by mutableStateOf(true)
    var selectGenerator by mutableStateOf(false)
    var selectOutput by mutableStateOf(false)

    var configGenerator by mutableStateOf(false)
    var configMasks by mutableStateOf(false)
    var configSlices by mutableStateOf(false)

    var squareGenerator by mutableStateOf(false)

    var mainMain by mutableStateOf(true)
    var exportSettings by mutableStateOf(false)
    var themeSettings by mutableStateOf(false)

    var screenWidth by mutableStateOf(1200.dp)
    var screenHeight by mutableStateOf(800.dp)

    var genTypeA by mutableStateOf("")
    var genTypeB by mutableStateOf("")

    // Display Settings
    var displayed by mutableStateOf(false)
    var nodeDisplay by mutableStateOf(true)

    // Card Animations
    var menuCardState by mutableStateOf(false) 
    var settingsCardState by mutableStateOf(false)
}