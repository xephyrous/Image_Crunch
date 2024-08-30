package ui

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

// Set of functions to make composable easier
// Passes inputs in a more user-friendly way when designing app

/**
 * Creates a row containing a button
 *
 * @param height The height of the element
 * @param width The width of the element
 * @param buttonHeight The height of the button within the element
 * @param buttonWidth The width of the button within the element
 * @param fontSize The default font size of the element
 * @param buttonEvent The function to run when the button is clicked
 * @param buttonText The text of the button
 */
@Composable
fun buttonElement(
    height: Dp = 50.dp,
    width: Dp = 300.dp,
    buttonHeight: Dp = 40.dp,
    buttonWidth: Dp = 250.dp,
    fontSize: TextUnit = 18.sp,
    buttonEvent: () -> Unit,
    buttonText: String,
) {
    Box(
        modifier = Modifier.size(height = height * ViewModel.yScale, width = width * ViewModel.xScale)
    ) {
        Button(
            onClick = buttonEvent,
            modifier = Modifier.size(height = buttonHeight * ViewModel.yScale, width = buttonWidth * ViewModel.xScale)
                .align(alignment = Alignment.Center),
            colors = ButtonDefaults.buttonColors(backgroundColor = ViewModel.themeColor.button)
        ) {
            Text(
                buttonText,
                color = ViewModel.themeColor.textColors["text2"] ?: Color(0),
                fontSize = fontSize * ViewModel.xScale.coerceAtMost(ViewModel.yScale)
            )
        }
    }
}

/**
 * Creates a row containing a text element
 *
 * @param height The height of the element box
 * @param width The width of the element box
 * @param displayedText The text to be displayed
 * @param textOffset The Y offset of the text in dot points (Dp)
 * @param fontSize The font size
 * @param font The font
 */
@Composable
fun textElement(
    height: Dp = 50.dp,
    width: Dp = 300.dp,
    displayedText: String,
    textOffset: Dp,
    fontSize: TextUnit,
    font: FontWeight = FontWeight.Normal,
) {
    Box(
        modifier = Modifier.size(height = height * ViewModel.yScale, width = width * ViewModel.xScale)
    ) {
        Text(
            text = displayedText,
            color = ViewModel.themeColor.textColors["text1"] ?: Color(0),
            modifier = Modifier.fillMaxSize().offset(y = (textOffset) * ViewModel.yScale)
                .align(alignment = Alignment.Center),
            fontSize = fontSize * ViewModel.xScale.coerceAtMost(ViewModel.yScale),
            textAlign = TextAlign.Center,
            fontWeight = font
        )
    }
}

/**
 * An animation pane for horizontally sliding in content
 *
 * @param visibility If the pane is displayed
 * @param animationWidth The width of the animation pane
 * @param duration The duration of the animation
 * @param paneContent The content of the pane
 */
@Composable
fun horizontalVisibilityPane(
    visibility: Boolean,
    animationWidth: Int = 2,
    duration: Int = 250,
    paneContent: @Composable (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        visible = visibility,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = duration)
        ) { fullWidth -> fullWidth * animationWidth },
        exit = slideOutHorizontally(
            tween(durationMillis = duration)
        ) { fullWidth -> fullWidth * animationWidth },
        content = paneContent
    )
}

/**
 * An animation pane for vertically sliding in content
 *
 * @param visibility If the pane is displayed
 * @param animationHeight The height of the animation pane
 * @param duration The duration of the animation
 * @param paneContent The content of the pane
 */
@Composable
fun verticalVisibilityPane(
    visibility: Boolean,
    animationHeight: Int = 2,
    duration: Int = 250,
    paneContent: @Composable (AnimatedVisibilityScope.() -> Unit)
) {
    AnimatedVisibility(
        visible = visibility,
        enter = slideInVertically(
            animationSpec = tween(durationMillis = duration)
        ) { fullHeight -> fullHeight * animationHeight },
        exit = slideOutVertically(
            tween(durationMillis = duration)
        ) { fullHeight -> fullHeight * animationHeight },
        content = paneContent
    )
}

/**
 * Creates a card GUI object
 *
 * @param xOffset The X offset of the card in dot points (Dp)
 * @param yOffset The Y offset of the card in dot points (Dp)
 * @param width The width of the card in dot points (Dp)
 * @param height The height of the card in dot points (Dp)
 * @param elevation The elevation of the card in dot points (Dp)
 * @param borderWidth The width of the card border
 * @param cardContent The content of the card
 */
@Composable
fun createCard(
    xOffset: Dp,
    yOffset: Dp,
    width: Dp,
    height: Dp,
    elevation: Dp,
    borderWidth: Dp = 1.dp,
    cardContent: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = Modifier.offset(xOffset * ViewModel.xScale, yOffset * ViewModel.yScale)
            .size(width * ViewModel.xScale, height * ViewModel.yScale),
        elevation = elevation,
        border = BorderStroke(borderWidth, ViewModel.themeColor.border),
        content = {
            Box(
                modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(
                            ViewModel.themeColor.card["cGrad1"] ?: Color(0),
                            ViewModel.themeColor.card["cGrad2"] ?: Color(0)
                        ),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f),
                        tileMode = TileMode.Clamp
                    )
                ).fillMaxSize(),
                content = cardContent
            )
        }
    )
}

// i reworked it, im not doccing it tho...
abstract class MenuItem(
    val itemTitle: String,
)

class MenuPage(
    val pageSize: Int,
    val menuPage: @Composable AnimatedVisibilityScope.() -> Unit,
    pageTitle: String
) : MenuItem(pageTitle)

class MenuButton(
    val buttonEvent: () -> Unit,
    pageTitle: String
) : MenuItem(pageTitle)

class SettingsMenu(
    private val menuItems: List<MenuItem>,
    private val name: String
) {
    companion object {
        private var openPages: ArrayList<SettingsMenu> = arrayListOf()
    }

    var isOpen = false

    // Controllers
    private var onHome = true
    private var pageNum by mutableStateOf(0)
    private var currentSize by mutableStateOf(menuItems.size)

    fun openMenu() {
        openPages.add(this)
        this.isOpen = true
    }

    fun closeMenu() {
        openPages.remove(this)
        this.isOpen = false
    }

    @Composable
    fun createMenu() {
        // Animated Offsets
        val xOffset by animateDpAsState(if (this.isOpen) 10.dp + (openPages.indexOf(this) * 310.dp) else (-310).dp)
        val size by animateDpAsState(currentSize.coerceAtLeast(1) * 50.dp)

        // Menu
        createCard(
            xOffset = xOffset, yOffset = 5.dp, width = 245.dp,
            height = 50.dp, elevation = 20.dp, borderWidth = 1.dp,
            cardContent = {
                textElement(
                    displayedText = name, textOffset = 10.dp,
                    fontSize = 30.sp, font = FontWeight.Normal,
                )
            }
        )
        createCard(
            xOffset = xOffset + (250.dp), yOffset = 5.dp, width = 50.dp,
            height = 50.dp, elevation = 20.dp, borderWidth = 1.dp,
            cardContent = {
                IconButton(onClick = { closeMenu() }, modifier = Modifier.align(Alignment.Center)) {
                    Icon(
                        imageVector = Icons.Sharp.Close,
                        contentDescription = "Close Button",
                        tint = ViewModel.themeColor.icon
                    )
                }
            }
        )
        createCard(
            xOffset = xOffset,
            yOffset = (if (onHome) (60.dp) else (size + 65.dp)),
            width = 300.dp, height = 50.dp, elevation = 20.dp, borderWidth = 1.dp,
            cardContent = {
                buttonElement(
                    buttonEvent = {
                        currentSize = menuItems.size
                        onHome = true
                    }, buttonText = "Return to Home"
                )
            }
        )
        createCard(
            xOffset = xOffset, yOffset = 60.dp,
            width = 300.dp, height = size, elevation = 20.dp, borderWidth = 1.dp,
            cardContent = {
                horizontalVisibilityPane(
                    visibility = (onHome),
                    animationWidth = 2,
                    duration = 369,
                    paneContent = {
                        LazyColumn {
                            items(menuItems.size) { item ->
                                when (menuItems[item]) {
                                    is MenuPage -> {
                                        buttonElement(
                                            buttonText = menuItems[item].itemTitle,
                                            buttonEvent = {
                                                onHome = false
                                                currentSize = (menuItems[item] as MenuPage).pageSize
                                                pageNum = item
                                            }
                                        )
                                    }

                                    is MenuButton -> {
                                        buttonElement(
                                            buttonText = menuItems[item].itemTitle,
                                            buttonEvent = (menuItems[item] as MenuButton).buttonEvent
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
                for (i in menuItems.indices) {
                    if (menuItems[i] is MenuPage) {
                        horizontalVisibilityPane(
                            visibility = (pageNum == i && !onHome),
                            animationWidth = 2,
                            duration = 369,
                            paneContent = (menuItems[i] as MenuPage).menuPage
                        )
                    }
                }
            }
        )
    }
}