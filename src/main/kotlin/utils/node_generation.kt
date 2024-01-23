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
fun squareNodeGenerator(list: ParameterList) : ArrayList<PositionNode> {
    val nodeList: ArrayList<PositionNode> = ArrayList()

    val imageSize = list.at("imageSize") as Dimension;
    val xSpacing: Int  = imageSize.width / (list.at("columns") as Int);
    val ySpacing: Int  = imageSize.height / (list.at("rows") as Int);

    for(y in 0 until (list.at("rows") as Int)) {
        for(x in 0 until (list.at("columns") as Int)) {
            nodeList.add(PositionNode((x + 1) * xSpacing, (y + 1) * ySpacing));
        }
    }

    return nodeList
}
