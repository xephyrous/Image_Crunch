package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import utils.images.*
import utils.storage.GeneratorType
import utils.storage.Global

/**
 * Updates the node mask for the displayed image
 *
 * @param vm The ViewModel object of the application
 * @param type The type of generator used to generate the mask nodes
 */
fun updateMask(vm: ViewModel, type: GeneratorType) {
    vm.nodeDisplay = false
    vm.displayedNodes = createNodeMask(
        generateNodes(type)
    )
    vm.nodeInputStream = loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedNodes!!))
    vm.nodeBitmapPainter = BitmapPainter(vm.nodeInputStream!!)
    vm.nodeDisplay = true
}

/**
 * Builds the GUI of the bottom bar settings panels
 *
 * @param filler god
 */
@Suppress("DuplicatedCode")
@Composable
fun bottomBar(vm: ViewModel) {
    createCard(
        (vm.screenWidth - 50.dp),
        (vm.screenHeight - 150.dp),
        50.dp,
        50.dp,
        10.dp,
        cardContent = {
            IconButton(
                onClick = {
                    if (Global.loadedImage.value == null) {
                        AlertBox.DisplayAlert("Please select an image", 3002)
                    } else if (Global.outputLocation == null) {
                        AlertBox.DisplayAlert("Please select an output location", 3001)
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
                    tint = Color(ViewModel.themeColor.icon)
                )
            }
        }
    )
    createCard(
        0.dp,
        (vm.screenHeight - 100.dp),
        vm.screenWidth,
        100.dp,
        10.dp,
        cardContent = {

        }
    ) // Baseplate
}