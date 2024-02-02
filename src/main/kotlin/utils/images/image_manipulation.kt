package utils.images

import utils.storage.PositionNode
import utils.storage.squareColumns
import utils.storage.squareRows

//List of node generation functions
enum class NodeGeneratorType {
    SQUARE,
}

//This function calls as the first step in image processing,
//TODO : Get function parameters from UI elements
//TODO : Create UI menus for each generator type

fun generateNodes(genType: NodeGeneratorType) : ArrayList<PositionNode> {
    return when (genType) {
        NodeGeneratorType.SQUARE -> squareNodeGenerator(squareRows, squareColumns)
        else -> {
            squareNodeGenerator(1, 1)
        }
    }
}