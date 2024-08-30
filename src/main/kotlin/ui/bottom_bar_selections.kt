package ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import utils.images.*
import utils.storage.GeneratorType
import utils.storage.Global

/**
 * Updates the node mask for the displayed image
 *
 * @param type The type of generator used to generate the mask nodes
 */
fun updateMask(type: GeneratorType) {
    ViewModel.nodeDisplay = false
    ViewModel.displayedNodes = createNodeMask(
        generateNodes(type)
    )
    ViewModel.nodeInputStream = loadImageBitmap(inputStream = bufferedImageToOutputStream(ViewModel.displayedNodes!!))
    ViewModel.nodeBitmapPainter = BitmapPainter(ViewModel.nodeInputStream!!)
    ViewModel.nodeDisplay = true
}

object BottomBar {
    /**
     * Builds the GUI of the bottom bar settings panels
     *
     */
    @Suppress("DuplicatedCode")
    @Composable
    fun createBottomBar() {
        createCard(
            (ViewModel.screenWidth - 50.dp),
            (ViewModel.screenHeight - 150.dp),
            50.dp,
            50.dp,
            10.dp,
            cardContent = {
                IconButton(
                    onClick = {
                        if (Global.loadedImage.value == null) {
                            AlertBox.displayAlert("Please select an image", 3002)
                        } else if (Global.outputLocation == null) {
                            AlertBox.displayAlert("Please select an output location", 3001)
                        } else {
                            Global.loadedImage.lock()
                            Global.loadedImageSize.lock()
                            Global.squareRows.lock()
                            Global.squareColumns.lock()
                            Global.generatorType.lock()
                            Global.nodes.lock()
                            Global.mask.lock()

                            Global.slices.value = runImagePipeline(Global.generatorType.value)

                            Global.slices.lock()

                            for (i in Global.slices.value!!.indices) {
                                maskToImage(Global.loadedImage.value!!, Global.slices.value!![i], "Output-${i}")
                            }

                            Global.loadedImage.unlock()
                            Global.loadedImageSize.unlock()
                            Global.squareRows.unlock()
                            Global.squareColumns.unlock()
                            Global.generatorType.unlock()
                            Global.nodes.unlock()
                            Global.mask.unlock()
                            Global.slices.unlock()
                        }
                    }) {
                    Icon(
                        imageVector = Icons.Sharp.PlayArrow,
                        contentDescription = "Play",
                        tint = ViewModel.themeColor.icon
                    )
                }
            }
        )
        createCard(
            0.dp,
            (ViewModel.screenHeight - 100.dp),
            ViewModel.screenWidth,
            100.dp,
            10.dp,
            cardContent = {

            }
        ) // Base plate
    }
}