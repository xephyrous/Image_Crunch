package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * The quickstart guide / help menu for the application
 */
object HelpMenu {
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
     * @param borderWidth The width of the card border
     */
    @Composable
    fun CreateHelpMenu(
        screenWidth: Dp,
        screenHeight: Dp,
        borderWidth: Dp = 1.dp,
    ) {
        AnimatedVisibility(
            visible = displayed,
            enter = scaleIn(),
            exit = scaleOut(),
        ) {
            var offsetX by remember { mutableStateOf(screenWidth / 5) }
            var offsetY by remember { mutableStateOf(screenHeight / 5) }

            // Title Bar
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY)
                    .size(545.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize(),
                ) {
                    textElement(
                        displayedText = pageTitles[page], textOffset = 10.dp,
                        height = 50.dp, width = 545.dp,
                        fontSize = 30.sp, font = FontWeight.Normal,
                    )
                }
            }
            Card(
                modifier = Modifier
                    .offset(offsetX + 550.dp, offsetY)
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize(),
                ) {
                    IconButton(onClick = { displayed = false }) {
                        Icon(
                            imageVector = Icons.Sharp.Close,
                            contentDescription = "Close Button",
                            tint = Color(ViewModel.themeColor.icon)
                        )
                    }
                }
            }

            // Body
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY + 55.dp)
                    .size(600.dp, 400.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize(),
                ) {
                    when (page) {
                        0 -> {
                            Column {
                                Image(
                                    painter = painterResource("assets/pic1.png"),
                                    contentDescription = "Picture of the top bar with a red circle",
                                    modifier = Modifier
                                        .offset(y = 10.dp)
                                        .height(290.dp)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                                textElement(
                                    displayedText = "App Settings can be accessed by pressing the main menu button in the top right corner",
                                    height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                    fontSize = 25.sp, font = FontWeight.Normal
                                )
                            }
                        }

                        1 -> {
                            Column {
                                Image(
                                    painter = painterResource("assets/pic2.png"),
                                    contentDescription = "Picture of the top bar with a red circle",
                                    modifier = Modifier
                                        .offset(y = 10.dp)
                                        .height(290.dp)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                                textElement(
                                    displayedText = "Image generation settings can be altered with the settings menu in the top right corner",
                                    height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                    fontSize = 25.sp, font = FontWeight.Normal,
                                )
                            }
                        }

                        2 -> {
                            textElement(
                                displayedText = "Within the settings menu we can configure how to use the generators",
                                height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal
                            )
                        }

                        3 -> {
                            Column {
                                Image(
                                    painter = painterResource("assets/pic4.png"),
                                    contentDescription = "Picture of the top bar with a red circle",
                                    modifier = Modifier
                                        .offset(y = 10.dp)
                                        .height(290.dp)
                                        .fillMaxSize(),
                                    contentScale = ContentScale.Fit
                                )
                                textElement(
                                    displayedText = "An image can be chosen by clicking the plus button in the top right",
                                    height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                    fontSize = 25.sp, font = FontWeight.Normal
                                )
                            }
                        }

                        4 -> {
                            textElement(
                                displayedText = "Once you have configured each setting to your liking, press the run button to generate",
                                height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal
                            )
                        }

                        5 -> {
                            textElement(
                                displayedText = "You can export your generation settings in the \"Export Settings\" tab of the main menu",
                                height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal
                            )
                        }

                        6 -> {
                            textElement(
                                displayedText = "You can view this help menu again by pressing \"GET HELP\" in the main menu",
                                height = 100.dp, width = 600.dp, textOffset = 10.dp,
                                fontSize = 25.sp, font = FontWeight.Normal
                            )
                        }
                    }
                }
            }
            // Bottom Bar
            Card(
                modifier = Modifier
                    .offset(offsetX, offsetY + 460.dp)
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize(),
                ) {
                    IconButton(onClick = { if (page > 0) page-- }) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowLeft,
                            contentDescription = "Close Button",
                            tint = Color(ViewModel.themeColor.icon)
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .offset(offsetX + 55.dp, offsetY + 460.dp)
                    .size(490.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize()
                ) {
                    textElement(
                        displayedText = "Welcome To Image Crunch - Page: ${page + 1}", textOffset = 10.dp,
                        height = 50.dp, width = 490.dp,
                        fontSize = 25.sp, font = FontWeight.Normal
                    )
                }

            }
            Card(
                modifier = Modifier
                    .offset(offsetX + 550.dp, offsetY + 460.dp)
                    .size(50.dp, 50.dp)
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x.toDp()
                            offsetY += dragAmount.y.toDp()
                        }
                    },
                elevation = 15.dp,
                border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
            ) {
                Box(
                    modifier = Modifier.background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(ViewModel.themeColor.card["cGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["cGrad2"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    ).fillMaxSize(),
                ) {
                    IconButton(onClick = { if (page < 6) page++ }) {
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowRight,
                            contentDescription = "Close Button",
                            tint = Color(ViewModel.themeColor.icon)
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