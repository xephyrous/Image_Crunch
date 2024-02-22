package utils.images

import utils.storage.*

//This function calls as the first step in image processing,
//TODO : Get function parameters from UI elements
//TODO : Create UI menus for each generator type

fun generateNodes(genType: GeneratorType) : ArrayList<PositionNode> {
    return when (genType) {
        GeneratorType.SQUARE -> squareNodeGenerator(squareRows.value(), squareColumns.value())
        else -> {
            squareNodeGenerator(squareRows.value(), squareColumns.value())
        }
    }
}

fun generateCuts(genType: GeneratorType) : Mask {
    return when (genType) {
        GeneratorType.SQUARE -> squareCutGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        else -> {
            squareCutGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        }
    }
}

fun generateMasks(genType: GeneratorType) : ArrayList<Mask> {
    return when (genType) {
        GeneratorType.SQUARE -> squareMaskGenerator(mask.value()!!, nodes.value()!!, squareRows.value(), squareColumns.value())
        else -> {
            squareMaskGenerator(mask.value()!!, nodes.value()!!, squareRows.value(), squareColumns.value())
        }
    }
}