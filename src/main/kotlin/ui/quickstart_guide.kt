package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material.icons.sharp.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The quickstart guide / help menu for the application
 */
class HelpMenu {
    private var page by mutableStateOf(0)
    private val pageTitles = listOf(
        "Accessing the App",
        "Configuring Selections",
        "Altering Settings",
        "Selecting an Image",
        "Generating Your First Image",
        "Exporting Settings",
        "Have Fun!"
    )
    private var displayed by mutableStateOf(false)

    /**
     * Creates the HelpMenu GUI object
     * @param screenWidth The screen width in dot points (Dp)
     * @param screenHeight The screen height in dot points (Dp)
     * @param themeColor The current theme colors
     * @param cardGrad1 The first color tof the card gradient
     * @param cardGrad2 The first color tof the card gradient
     * @param borderWidth The width of the card border
     * @param borderColor The color of the card border
     * @param iconColor The color of the card icon
     * @param textColor The color of the card text
     */
    @Composable
    fun CreateHelpMenu(
        screenWidth: Dp,
        screenHeight: Dp,
        themeColor: List<Color>,
        cardGrad1: Int = 8,
        cardGrad2: Int = 9,
        borderWidth: Dp = 1.dp,
        borderColor: Int = 18,
        iconColor: Int = 4,
        textColor: Int = 2
    ) {
        AnimatedVisibility(
            visible = displayed,
            enter = scaleIn(),
            exit = scaleOut(),
        ){
            var offsetX by remember { mutableStateOf(screenWidth/5) }
            var offsetY by remember { mutableStateOf(screenHeight/5) }

            // Title Bar
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .size(screenWidth/2 - 55.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize(),
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = pageTitles[page], textOffset = 10.dp,
                        fontSize = 30.sp, font = FontWeight.Normal,
                        themeColor = themeColor, textColor = textColor
                    )
                }
            }
            Card(
                modifier = Modifier
                    .offset(offsetX + (screenWidth/2 - 50.dp), offsetY)
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize(),
                ) {
                    IconButton(onClick = { displayed = false }) {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = "Close Button",
                            tint = themeColor[iconColor]
                        )
                    }
                }
            }

            // Body
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY+55.dp)
                    .size(screenWidth/2, screenHeight/2)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize(),
                ) {
                    when (page) {
                        0 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "App Settings can be accessed by pressing the main menu button in the top right corner", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        1 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "Many of the image generation settings can be altered with the settings button in the top right corner", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        2 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "Within the settings menu we can configure how to use the generators", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        3 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "An image can be chosen by clicking the plus button in the top right", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        4 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "Once you have configured each setting to your liking, press the run button to generate", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        5 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "You can export your generation settings in the \"Export Settings\" tab of the main menu", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                        6 -> {
                            textRow(
                                rowOffset = 2.5.dp, displayedText = "You can view this help menu again by pressing \"GET HELP\" in the main menu", textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal,
                                themeColor = themeColor, textColor = textColor
                            )
                        }
                    }
                }
            }
            // Bottom Bar
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY+(screenHeight/2)+60.dp)
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize(),
                ) {
                    IconButton(onClick = { if (page > 0) page-- }) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowLeft,
                            contentDescription = "Close Button",
                            tint = themeColor[iconColor]
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .offset(offsetX + 55.dp, offsetY+60.dp + (screenHeight/2))
                    .size(screenWidth/2- 110.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize()
                ) {
                    textRow(
                        rowOffset = 2.5.dp, displayedText = "Welcome To Image Crunch - Page: ${page+1}", textOffset = 10.dp,
                        fontSize = 25.sp, font = FontWeight.Normal,
                        themeColor = themeColor, textColor = textColor
                    )
                }

            }
            Card(
                modifier = Modifier
                    .offset(offsetX + (screenWidth/2 - 50.dp), offsetY+60.dp + (screenHeight/2))
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, themeColor[borderColor]),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )).fillMaxSize(),
                ){
                    IconButton(onClick = { if (page < 5) page++ }) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowRight,
                            contentDescription = "Close Button",
                            tint = themeColor[iconColor]
                        )
                    }
                }
            }
        }
    }

    /**
     * Displays the HelpMenu GUI object
     */
    fun ShowHelpMenu() {
        page = 0
        displayed = true
    }
}