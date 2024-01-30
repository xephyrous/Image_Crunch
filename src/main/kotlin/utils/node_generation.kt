package utils

import androidx.compose.material.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import java.awt.Dimension
import kotlin.math.ceil

// Node Generation Functions must be (list: ParameterList) -> ArrayList<PositionNode>

/**
 * @param rows Desired number of rows
 * @param columns Desired number of columns
 * @return List of PositionNodes for the image in a square grid
 *
 * TODO Add fix for las row/column jamming due to ceil()
 */
fun squareNodeGenerator(
    rows: Int, columns: Int
) : ArrayList<PositionNode> {
    val nodeList: ArrayList<PositionNode> = ArrayList()

    val xSpacing: Int  = ceil(loadedImageSize.width / columns.toDouble()).toInt()
    val ySpacing: Int  = ceil(loadedImageSize.height / rows.toDouble()).toInt()

    for(y in 0 .. rows) {
        for(x in 0 .. columns) {
            nodeList.add(
                PositionNode(
                    (x * xSpacing).coerceAtMost(loadedImageSize.width - 1),
                    (y * ySpacing).coerceAtMost(loadedImageSize.height - 1)
                )
            )
        }
    }

    return nodeList
}
