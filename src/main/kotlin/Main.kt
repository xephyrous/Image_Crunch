/*
 * Project Timeline : 1/17/2024 -> TBD (4/1/2024)
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
import ui.grabThemes

fun main() = application {
    // parseConfigData()
    // parseThemeData
    // TODO : Setup pkl codegen-kotlin implementation (Undocumented Gradle hell)
    // is that my bad? -aidan

    println(grabThemes("src/main/config/themes"))

    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(150.dp, 150.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}