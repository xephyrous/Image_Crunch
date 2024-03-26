package utils.images

import utils.storage.*

// This function calls as the first step in image processing,
// TODO : Get function parameters from UI elements
// TODO : Create UI menus for each generator type

fun runImagePipeline(genType: GeneratorType): ArrayList<Mask>? {
    when(genType) {
        GeneratorType.NONE -> TODO("literally nothing")
        GeneratorType.SQUARE -> {
            nodes.set(generateNodes(GeneratorType.SQUARE))
            return generateMasks(GeneratorType.SQUARE)
        }
        else -> {
            return null
        }
    }
}

fun generateNodes(genType: GeneratorType) : ArrayList<PositionNode> {
    return when (genType) {
        GeneratorType.SQUARE -> squareNodeGenerator(squareRows.value(), squareColumns.value())
        else -> {
            squareNodeGenerator(squareRows.value(), squareColumns.value())
        }
    }
}

private fun generateCuts(genType: GeneratorType) : Mask? {
    return when (genType) {
        GeneratorType.SQUARE_NOISE -> TODO("WAHHH")
        else -> {
            TODO("WAHHH 2.0")
        }
    }
}

private fun generateMasks(genType: GeneratorType) : ArrayList<Mask> {
    return when (genType) {
        GeneratorType.SQUARE -> squareMaskGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        else -> {
            squareMaskGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        }
    }
}