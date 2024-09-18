package ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.compose.dnd.reorder.ReorderContainer
import com.mohamedrejeb.compose.dnd.reorder.ReorderableItem
import com.mohamedrejeb.compose.dnd.reorder.rememberReorderState
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
    var selectedFilter by mutableStateOf(0)

    /**
     * Builds the GUI of the bottom bar settings panels
     *
     */
    @Suppress("DuplicatedCode")
    @Composable
    fun createBottomBar() {
        createCard(
            0.dp,
            (ViewModel.screenHeight - 150.dp),
            ViewModel.screenWidth - 50.dp,
            50.dp,
            10.dp,
            cardContent = {
                val scope = rememberCoroutineScope()
                val reorderState = rememberReorderState<String>()
                val lazyListState = rememberLazyListState()

                ReorderContainer(
                    state = reorderState,
                    enabled = true,
                ) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        state = lazyListState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(ViewModel.filters.size, key = { it }) { item ->
                            ReorderableItem(
                                state = reorderState,
                                key = item,
                                data = ViewModel.filters[item].name,
                                onDrop = {},
                                onDragEnter = {}
                            ) {
                                buttonElement(
                                    width = 50.dp,
                                    buttonWidth = 40.dp,
                                    buttonText = ViewModel.filters[item].name,
                                    buttonEvent = { selectedFilter = item }
                                )
                            }
                        }
                    }
                }
            }
        )
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
                    }, modifier = Modifier.align(Alignment.Center)
                ) {
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
            250.dp,
            100.dp,
            10.dp,
            cardContent = {
                Row(
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(
                        onClick = {
                            if (!(ViewModel.filters.isEmpty() || selectedFilter <= 0)) {
                                selectedFilter--
                            }
                        }, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(50.dp * ViewModel.xScale, 50.dp * ViewModel.yScale)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowLeft,
                            contentDescription = "Play",
                            tint = ViewModel.themeColor.icon,
                            modifier = Modifier.size(25.dp * ViewModel.xScale, 25.dp * ViewModel.yScale)
                        )
                    }
                    buttonElement(
                        100.dp,
                        150.dp,
                        80.dp,
                        140.dp,
                        buttonText = if (ViewModel.filters.isEmpty()) "No Filters\nSelected" else "Filter: ${selectedFilter + 1}",
                        buttonEvent = {
                            // do nothing?
                        }
                    )
                    IconButton(
                        onClick = {
                            if (!(ViewModel.filters.isEmpty() || selectedFilter >= ViewModel.filters.size - 1)) {
                                selectedFilter++
                            }
                        }, modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .size(50.dp * ViewModel.xScale, 50.dp * ViewModel.yScale)
                    ) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowRight,
                            contentDescription = "Play",
                            tint = ViewModel.themeColor.icon,
                            modifier = Modifier.size(25.dp * ViewModel.xScale, 25.dp * ViewModel.yScale)
                        )
                    }
                }
            }
        )
        createCard(
            250.dp,
            (ViewModel.screenHeight - 100.dp),
            ViewModel.screenWidth - 250.dp,
            100.dp,
            10.dp,
            cardContent = {
                for (i in ViewModel.filters.indices) {
                    verticalVisibilityPane(
                        visibility = (selectedFilter == i),
                        animationHeight = 2,
                        duration = 369,
                        paneContent = { ViewModel.filters[i].display() }
                    )
                }
            }
        ) // Base plate
    }
}