package utils

import androidx.compose.material.*
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable

// Node Generation Functions must be (list: ParameterList) -> ArrayList<PositionNode>

/*
 * "nodeCount" : Int
 */
fun squareNodeGenerator(list: ParameterList) : ArrayList<PositionNode> {
    val nodeList: ArrayList<PositionNode> = ArrayList()

    for(i in 0.. (list.at("nodeCount") as Int)) {
        //Modify nodes
        nodeList.add(PositionNode(i, i))
        println("AHH")
    }

    return nodeList
}
