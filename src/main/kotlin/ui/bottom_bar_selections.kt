package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.images.bufferedImageToOutputStream
import utils.images.createNodeMask
import utils.images.generateNodes
import utils.storage.GeneratorType
import utils.storage.Global.cutNoise
import utils.storage.Global.squareColumns
import utils.storage.Global.squareRows

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
 * @param vm The ViewModel object of the application
 * @param bottomCardsX The width of cards area in dot points (Dp)
 * @param bottomCardsY The height of the cards area in dot points (Dp)
 */
@Suppress("DuplicatedCode")
@Composable
fun bottomBar(vm: ViewModel) {
    val numbersOnly = Regex("^\\d+\$")

    verticalVisibilityPane(
        visibility = vm.configGenerator, animationHeight = 2, duration = 369, paneContent = {
            createCard(
                xOffset = 0.dp, yOffset = vm.screenHeight-220.dp,
                xScale = vm.xScale, yScale = vm.yScale,
                width = vm.screenWidth/3, height = 500.dp, elevation = 5.dp,
                themeColor = vm.themeColor, borderWidth = 1.dp,
                cardContent = {
                    // card content i GUESS
                    // TODO: fix this to be not trash (i'm talking to you future aidan...)
                    verticalVisibilityPane(
                        visibility = vm.selectedGenerator == 1, animationHeight = 2, duration = 369, paneContent = {
                            var sRT by mutableStateOf(squareRows.value().toString())
                            var sCT by mutableStateOf(squareColumns.value().toString())
                            Row() {
                                Text(
                                    "Number of Rows:", color = vm.themeColor[2],
                                    modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                    fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                )
                            }
                            Row(
                                modifier = Modifier.offset(y = 40.dp*vm.yScale)
                            ) {
                                TextField(
                                    value = sRT,
                                    onValueChange = {
                                        if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                            sRT = it
                                            if(sRT.isNotEmpty()) {
                                                squareRows.set(sRT.toInt())
                                            }
                                        }
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = vm.themeColor[16],
                                        disabledTextColor = vm.themeColor[17],
                                        backgroundColor = vm.themeColor[14],
                                        cursorColor = vm.themeColor[15],
                                        focusedIndicatorColor = vm.themeColor[12],
                                        unfocusedIndicatorColor = vm.themeColor[13]
                                    ),
                                    modifier = Modifier.size(((vm.screenWidth/3)-10.dp)*vm.xScale, 50.dp*vm.yScale)
                                        .offset(5.dp*vm.xScale, 5.dp*vm.yScale)
                                        .onKeyEvent {
                                            // THERE IS NO GOD
                                            if (it.key == Key.Enter) {
                                                if(squareRows.value() > 0 && vm.displayedImage != null) {
                                                    updateMask(vm, GeneratorType.SQUARE)
                                                }
                                            }
                                            true
                                        }
                                        .onFocusChanged {
                                            if (!it.isFocused) {
                                                if(squareRows.value() > 0 && vm.displayedImage != null) {
                                                    updateMask(vm, GeneratorType.SQUARE)
                                                }
                                            }
                                        }
                                )
                            }
                            Row(
                                modifier = Modifier.offset(y = 100.dp*vm.yScale)
                            ) {
                                Text(
                                    "Number of Columns:", color = vm.themeColor[2],
                                    modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                    fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                )
                            }
                            Row(
                                modifier = Modifier.offset(y = 140.dp*vm.yScale)
                            ) {
                                TextField(
                                    value = sCT,
                                    onValueChange = {
                                        if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                            sCT = it
                                            if(sCT.isNotEmpty()) squareColumns.set(sCT.toInt())
                                        }
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = vm.themeColor[16],
                                        disabledTextColor = vm.themeColor[17],
                                        backgroundColor = vm.themeColor[14],
                                        cursorColor = vm.themeColor[15],
                                        focusedIndicatorColor = vm.themeColor[12],
                                        unfocusedIndicatorColor = vm.themeColor[13]
                                    ),
                                    modifier = Modifier.size(((vm.screenWidth/3)-10.dp)*vm.xScale, 50.dp*vm.yScale)
                                        .offset(5.dp*vm.xScale, 5.dp*vm.yScale)
                                        .onKeyEvent {
                                            if (it.key == Key.Enter) {
                                                if(squareColumns.value() > 0 && vm.displayedImage != null) {
                                                    updateMask(vm, GeneratorType.SQUARE)
                                                }
                                            }
                                            true
                                        }
                                        .onFocusChanged {
                                            if (!it.isFocused) {
                                                if(squareColumns.value() > 0 && vm.displayedImage != null) {
                                                    updateMask(vm, GeneratorType.SQUARE)
                                                }
                                            }
                                        }
                                )
                            }
                        }
                    )
                }
            )
        }
    )
    verticalVisibilityPane(
        visibility = vm.configMasks, animationHeight = 2, duration = 369, paneContent = {
            createCard(
                xOffset = vm.screenWidth/3, yOffset = vm.screenHeight-150.dp,
                xScale = vm.xScale, yScale = vm.yScale,
                width = vm.screenWidth/3, height = 400.dp, elevation = 5.dp,
                themeColor = vm.themeColor, borderWidth = 1.dp,
                cardContent = {}
            )
        }
    )
    verticalVisibilityPane(
        visibility = vm.configSlices, animationHeight = 2, duration = 369, paneContent = {
            createCard(
                xOffset = (vm.screenWidth/3)*2, yOffset = vm.screenHeight-150.dp,
                xScale = vm.xScale, yScale = vm.yScale,
                width = vm.screenWidth/3, height = 400.dp, elevation = 5.dp,
                themeColor = vm.themeColor, borderWidth = 1.dp,
                cardContent = {
                    var sliderPosition by remember { mutableStateOf(cutNoise.value()) }
                    Column {
                        textElement(
                            width = vm.screenWidth/3, displayedText = "Cut Noise", xScale = vm.xScale, yScale = vm.yScale,
                            fontSize = 40.sp, textOffset = 5.dp, themeColor = vm.themeColor
                        )
                        Box (
                            modifier = Modifier.size(width = vm.screenWidth/3*vm.xScale, height = 40.dp*vm.yScale)
                        ) {
                            Slider(
                                value = sliderPosition,
                                onValueChange = {
                                    sliderPosition = it
                                    cutNoise.set(it)
                                    println(it)
                                },
                                colors = SliderDefaults.colors(
                                    thumbColor = vm.themeColor[14],
                                    activeTrackColor = vm.themeColor[13], // TODO: make these colors separate later
                                    inactiveTrackColor = vm.themeColor[12],
                                ),
                                modifier = Modifier.size(width = (vm.screenWidth/3-30.dp)*vm.xScale, 40.dp*vm.yScale).offset(x = 15.dp*vm.xScale)
                            )
                        }
                        textElement(
                            width = vm.screenWidth/3, displayedText = "Current Noise: $sliderPosition",
                            xScale = vm.xScale, yScale = vm.yScale,
                            fontSize = 30.sp, textOffset = 5.dp, themeColor = vm.themeColor
                        )
                    }
                }
            )
        }
    )
}