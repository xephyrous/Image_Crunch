/**
 * @author Alexander Yauchler
 * @author Aidan Mao
 *
 * Project Timeline : 1/17/2024 -> TBD (3/15/2024)
 *
 * This software is under the MIT License
 *
 * Written by Alexander Yauchler and Aidan Mao
 * Freshmen in Computer Science at Purdue University Fort Wayne
 *
 * Project direction by Dr. Chen
 */

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import ui.App
import utils.images.runImagePipeline
import utils.storage.GeneratorType
import utils.storage.loadedImageSize
import java.awt.Dimension

fun main() = application {
    // parseConfigData()
    // parseThemeData()

    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(150.dp, 150.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}