package utils

import androidx.compose.material.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import java.awt.Dimension

// Node Generation Functions must be (list: ParameterList) -> ArrayList<PositionNode>

/*
 * "imageSize"  : Dimension
 * "rows"       : Int
 * "columns"    : Int
 */
fun squareNodeGenerator(rows: Int, columns: Int) : ArrayList<PositionNode> {
    val nodeList: ArrayList<PositionNode> = ArrayList()

    val xSpacing: Int  = loadedImageSize.width / columns
    val ySpacing: Int  = loadedImageSize.height /rows

    for(y in 0 .. rows) {
        for(x in 0 .. columns) {
            nodeList.add(PositionNode(x * xSpacing, y * ySpacing))
            println("<${x * xSpacing}, ${y * ySpacing}>")
        }
    }

    return nodeList
}
