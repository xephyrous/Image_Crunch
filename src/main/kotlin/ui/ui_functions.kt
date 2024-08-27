package ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
 * @param xScale The horizontal scaling multiplier of the elements
 * @param yScale The vertical scaling multiplier of the elements
 * @param buttonEvent The function to run when the button is clicked
 * @param buttonText The text of the button
 * @param themeColor A list of theme colors to use for styling the button
 * @param buttonColor The position of the button color in the [themeColor]
 * @param textColor The position of the text color in the [themeColor]
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
){
    Box(
        modifier = Modifier.size(height = height * ViewModel.yScale, width = width * ViewModel.xScale)
    ) {
        Button(
            onClick = buttonEvent,
            modifier = Modifier.size(height = buttonHeight * ViewModel.yScale, width = buttonWidth * ViewModel.xScale)
                .align(alignment = Alignment.Center),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(ViewModel.themeColor.button))
        ) {
            Text(
                buttonText,
                color = Color(ViewModel.themeColor.textColors["text2"] ?: 0),
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
 * @param xScale The horizontal scaling multiplier of the elements
 * @param yScale The vertical scaling multiplier of the elements
 * @param displayedText The text to be displayed
 * @param textOffset The Y offset of the text in dot points (Dp)
 * @param fontSize The font size
 * @param font The font
 * @param themeColor List of theme colors to use for styling the text
 * @param textColor The position of the text color in the [themeColor]
 */
@Composable
fun textElement(
    height: Dp = 50.dp,
    width: Dp = 300.dp,
    displayedText: String,
    textOffset: Dp,
    fontSize: TextUnit,
    font: FontWeight = FontWeight.Normal,
){
    Box(
        modifier = Modifier.size(height = height * ViewModel.yScale, width = width * ViewModel.xScale)
    ) {
        Text(
            text = displayedText,
            color = Color(ViewModel.themeColor.textColors["text1"] ?: 0),
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
){
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
){
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
 * @param themeColor A list of theme colors to use for styling the card
 * @param cardGrad1 Position of the start gradient color in the [themeColor]
 * @param cardGrad2 Position of the end gradient color in the [themeColor]
 * @param borderWidth The width of the card border
 * @param borderColor The position of the border color in the [themeColor]
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
        border = BorderStroke(borderWidth, Color(ViewModel.themeColor.border)),
        content = {
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
                    )).fillMaxSize(),
                content = cardContent
            )
        }
    )
}

/**
 * Creates a menu GUI object
 *
 * @param xOffset The horizontal offset of the menu object (Dp)
 * @param yOffset The vertical offset of the menu object (Dp)
 * @param width The normal size of the menu (Dp)
 * @param height The size of the main menu area (Dp)
 * @param titleSize The standard size of the title bar (Dp)
 * @param gapSize The size of the gap between the menu objects (Dp)
 * @param xScale The horizontal scaling multiplier of the elements
 * @param yScale The vertical scaling multiplier of the elements
 * @param page The menu page handler
 * @param elevation The elevation of the menu in dot points (Dp)
 * @param menuTitle The title of the menu
 * @param returnTitle The return button text
 * @param themeColor A list of theme colors to use for styling the menu
 * @param cardGrad1 Position of the start gradient in [themeColor]
 * @param cardGrad2 Position of the end gradient in [themeColor]
 * @param borderWidth The width of the menu border
 * @param borderColor The color of the menu border
 * @param buttonColor The position of the button color in [themeColor]
 * @param titleColor The position of the title color in [themeColor]
 * @param textColor The position of the text color in [themeColor]
 * @param iconColor the position of the icon color in [themeColor]
 * @param exitOperation The function to be run when the exit button is clicked
 * @param closeOperation The function to be run when the menu is closed
 * @param menuPages The GUI objects of the menu pages
 */
@Composable
fun createMenu(
    xOffset: Dp,
    yOffset: Dp,
    width: Dp,
    height: Dp,
    titleSize: Dp,
    gapSize: Dp,
    page: Int,
    elevation: Dp, // Elevation
    menuTitle: String, // Title
    returnTitle: String, // Return button text
    borderWidth: Dp = 1.dp,
    exitOperation: () -> Unit, // the exit button
    closeOperation: () -> Unit,
    menuPages: @Composable BoxScope.() -> Unit // the main menu pages
) {
    createCard(
        xOffset = xOffset, yOffset = yOffset, width = width - titleSize - gapSize,
        height = titleSize, elevation = elevation, borderWidth = borderWidth,
        cardContent = {
            textElement(
                displayedText = menuTitle, textOffset = 10.dp,
                fontSize = 30.sp, font = FontWeight.Normal,
            )
        }
    )
    createCard(
        xOffset = xOffset + (width - titleSize), yOffset = yOffset, width = titleSize,
        height = titleSize, elevation = elevation, borderWidth = borderWidth,
        cardContent = {
            IconButton(onClick = closeOperation, modifier = Modifier.align(Alignment.Center)) {
                Icon(
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "Close Button",
                    tint = Color(ViewModel.themeColor.icon),
                )
            }
        }
    )
    createCard(
        xOffset = xOffset,
        yOffset = (if (page == 0) yOffset + height else yOffset + height + titleSize + (gapSize * 2)),
        width = width, height = titleSize, elevation = elevation, borderWidth = borderWidth,
        cardContent = {
            buttonElement(
                buttonEvent = exitOperation, buttonText = returnTitle
            )
        }
    )
    createCard(
        xOffset = xOffset, yOffset = yOffset + gapSize + titleSize,
        width = width, height = height, elevation = elevation, borderWidth = borderWidth,
        cardContent = menuPages
    )
}