package ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.images.bufferedImageToOutputStream
import utils.images.createNodeMask
import utils.images.generateNodes
import utils.storage.GeneratorType
import utils.storage.squareColumns
import utils.storage.squareRows

fun updateMask(vm: ViewModel, type: GeneratorType) {
    vm.nodeDisplay = false
    vm.displayedNodes = createNodeMask(
        generateNodes(type)
    )
    vm.nodeInputStream = loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedNodes!!))
    vm.nodeBitmapPainter = BitmapPainter(vm.nodeInputStream!!)
    vm.nodeDisplay = true
}

@Suppress("DuplicatedCode")
@Composable
fun bottomBar(vm: ViewModel, bottomCardsX: Dp, bottomCardsY: Dp) {
    val numbersOnly = Regex("^\\d+\$")

    verticalVisibilityPane(
        visibility = vm.configGenerator, animationHeight = 2, duration = 369, paneContent = {
            createCard(
                xOffset = 0.dp, yOffset = bottomCardsY-220.dp,
                width = bottomCardsX/3, height = 500.dp, elevation = 5.dp,
                themeColor = vm.themeColor, cardColor = 5,
                cardContent = {
                    // card content i GUESS
                    verticalVisibilityPane(
                        visibility = vm.selectedGenerator == 1, animationHeight = 2, duration = 369, paneContent = {
                            Row() {
                                Text(
                                    "Number of Rows:", color = vm.themeColor[2],
                                    modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                    fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                )
                            }
                            Row(
                                modifier = Modifier.offset(y = 50.dp)
                            ) {
                                TextField(
                                    value = vm.genTypeA,
                                    onValueChange = {
                                        if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                            vm.genTypeA = it
                                            if(vm.genTypeA.isNotEmpty()) {
                                                squareRows.set(vm.genTypeA.toInt())
                                            }
                                        }
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = vm.themeColor[2],
                                        disabledTextColor = vm.themeColor[2],
                                        backgroundColor = vm.themeColor[1],
                                        cursorColor = vm.themeColor[2],
                                        focusedIndicatorColor = vm.themeColor[2],
                                        unfocusedIndicatorColor = vm.themeColor[1]
                                    ),
                                    modifier = Modifier.size((vm.screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp)
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
                                modifier = Modifier.offset(y = 110.dp)
                            ) {
                                Text(
                                    "Number of Columns:", color = vm.themeColor[2],
                                    modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                    fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                )
                            }
                            Row(
                                modifier = Modifier.offset(y = 160.dp)
                            ) {
                                TextField(
                                    value = vm.genTypeB,
                                    onValueChange = {
                                        if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                            vm.genTypeB = it
                                            if(vm.genTypeB.isNotEmpty()) squareColumns.set(vm.genTypeB.toInt())
                                        }
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = vm.themeColor[2],
                                        disabledTextColor = vm.themeColor[2],
                                        backgroundColor = vm.themeColor[1],
                                        cursorColor = vm.themeColor[2],
                                        focusedIndicatorColor = vm.themeColor[2],
                                        unfocusedIndicatorColor = vm.themeColor[1]
                                    ),
                                    modifier = Modifier.size((vm.screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp)
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
                xOffset = bottomCardsX/3, yOffset = bottomCardsY-150.dp,
                width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                themeColor = vm.themeColor, cardColor = 5,
                cardContent = {}
            )
        }
    )
    verticalVisibilityPane(
        visibility = vm.configSlices, animationHeight = 2, duration = 369, paneContent = {
            createCard(
                xOffset = (bottomCardsX/3)*2, yOffset = bottomCardsY-150.dp,
                width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                themeColor = vm.themeColor, cardColor = 5,
                cardContent = {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Filla Text", textOffset = 5.dp,
                        fontSize = 40.sp, font = FontWeight.Normal, themeColor = vm.themeColor, textColor = 2
                    )
                }
            )
        }
    )
}