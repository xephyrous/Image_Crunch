/*
 * Project Start : 1/17/2024
 * Project End   : TBD
 *
 * This software is under the MIT License
 *
 * Written by Alexander Yauchler and Aidan Mao
 * Freshmen in Computer Science at Purdue University Fort Wayne
 *
 * Project direction by Dr. Chen
 */

package utils

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import java.awt.Dimension

//List of node generation functions
enum class NodeGeneratorType {
    SQUARE,
}

//This function calls as the first step in image processing,
//TODO : Get function parameters from UI elements
//TODO : Create UI menus for each generator type

fun generateNodes(genType: NodeGeneratorType) {
    when (genType) {
        NodeGeneratorType.SQUARE -> squareNodeGenerator(5, 5)
        else -> {  }
    }
}