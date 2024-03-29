package utils.images

import utils.storage.*

// This function calls as the first step in image processing,
// TODO : Get function parameters from UI elements
// TODO : Create UI menus for each generator type

/**
 * The entry call to the image pipeline, runs all subsequent processing functions, depending on the GeneratorType
 *
 * @param genType The type of generator to process the image with
 *
 * @return An array of image masks making up the total image
 */
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

/**
 * Generates an array of PositionNodes for positional aid in the cutting step, depending on the GeneratorType
 *
 * @param genType The type of generator to generate nodes with
 *
 * @return An array of positions that nodes occur at
 */
fun generateNodes(genType: GeneratorType) : ArrayList<PositionNode> {
    return when (genType) {
        GeneratorType.SQUARE -> squareNodeGenerator(squareRows.value(), squareColumns.value())
        else -> {
            squareNodeGenerator(squareRows.value(), squareColumns.value())
        }
    }
}

/**
 * Generates a mask containing cuts separating the image into pieces, depending on the GeneratorType
 *
 * @param genType The type of generator to generate cuts with
 *
 * @return A mask of bits representing a cut (1) or nothing (0)
 */
private fun generateCuts(genType: GeneratorType) : Mask? {
    return when (genType) {
        GeneratorType.SQUARE_NOISE -> TODO("WAHHH")
        else -> {
            TODO("WAHHH 2.0")
        }
    }
}

/**
 * Generates an array of Masks that make up the image, as pieces, depending on the GeneratorType
 *
 * @param genType The type of generator to generate masks with
 *
 * @return An array of masks that make up the image, as pieces
 */
private fun generateMasks(genType: GeneratorType) : ArrayList<Mask> {
    return when (genType) {
        GeneratorType.SQUARE -> squareMaskGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        else -> {
            squareMaskGenerator(nodes.value()!!, squareRows.value(), squareColumns.value())
        }
    }
}