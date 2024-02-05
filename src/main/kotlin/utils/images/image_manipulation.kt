package utils.images

import utils.storage.GeneratorType
import utils.storage.PositionNode
import utils.storage.squareColumns
import utils.storage.squareRows

//This function calls as the first step in image processing,
//TODO : Get function parameters from UI elements
//TODO : Create UI menus for each generator type

fun generateNodes(genType: GeneratorType) : ArrayList<PositionNode> {
    return when (genType) {
        GeneratorType.SQUARE -> squareNodeGenerator(squareRows.value(), squareColumns.value())
        else -> {
            squareNodeGenerator(1, 1)
        }
    }
}