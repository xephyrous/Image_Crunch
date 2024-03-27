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

// CHANGE ROW CODE TO NOT BE ROWS OR SMTH :D
// basically make this a bit more expandable by rewriting it to make blocks that can be inserted into main handler, rather than a row

@Composable
fun buttonRow(
    buttonOffset: Dp = 25.dp,
    height: Dp = 50.dp,
    width: Dp = 250.dp,
    buttonEvent: () -> Unit,
    buttonText: String,
    themeColor: List<Color>,
    buttonColor: Int = 10,
    textColor: Int = 2,
){
    Row(
        modifier = Modifier.size(height = height, width = width+50.dp)
    ) {
        Button(
            onClick = buttonEvent,
            modifier = Modifier.offset(buttonOffset, 0.dp).width(width),
            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[buttonColor])
        ) {
            Text(buttonText, color = themeColor[textColor])
        }
    }
}

@Composable
fun textRow(
    height: Dp = 50.dp,
    displayedText: String,
    textOffset: Dp,
    fontSize: TextUnit,
    font: FontWeight = FontWeight.Normal,
    themeColor: List<Color>,
    textColor: Int = 1
){
    Row(
        modifier = Modifier.height(height = height)
    ) {
        Text(
            text = displayedText,
            color = themeColor[textColor],
            modifier = Modifier.fillMaxSize().offset(y = textOffset),
            fontSize = fontSize,
            textAlign = TextAlign.Center,
            fontWeight = font
        )
    }
}

@Composable
fun horizontalVisibilityPane(
    visibility: Boolean,
    animationWidth: Int = 2,
    duration: Int = 250,
    paneContent: @Composable() (AnimatedVisibilityScope.() -> Unit)
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

@Composable
fun verticalVisibilityPane(
    visibility: Boolean,
    animationHeight: Int = 2,
    duration: Int = 250,
    paneContent: @Composable() (AnimatedVisibilityScope.() -> Unit)
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

@Composable
fun createCard(
    xOffset: Dp,
    yOffset: Dp,
    width: Dp,
    height: Dp,
    elevation: Dp,
    themeColor: List<Color>,
    cardGrad1: Int = 8, // Card color
    cardGrad2: Int = 9,
    borderWidth: Dp = 1.dp,
    borderColor: Int = 18,
    cardContent: @Composable BoxScope.()-> Unit
) {
    Card(
        modifier = Modifier.offset(xOffset, yOffset).size(width, height).background(
            Brush.linearGradient(
                colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, 0f),
                tileMode = TileMode.Clamp
            )),
        elevation = elevation,
        border = BorderStroke(borderWidth, themeColor[borderColor]),
        content = {
            Box(
                modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(themeColor[cardGrad1], themeColor[cardGrad2]),
                        start = Offset(0f, 0f),
                        end = Offset(Float.POSITIVE_INFINITY, 0f),
                        tileMode = TileMode.Clamp
                    )).fillMaxSize(),
                content = cardContent
            )
        }
    )
}

@Composable
fun createMenu(
    menuOffset: Dp, // x Location
    titleOffset: Dp, // Title Y
    mainOffset: Dp, // Main Y
    returnOffset: Dp, // Exit Y
    menuWidth: Dp, // Menu Width
    mainHeight: Dp, // Main menu height (titles are static 50)
    elevation: Dp, // Elevation
    menuTitle: String, // Title
    returnTitle: String, // Return button text
    themeColor: List<Color>, // Theme colors
    cardGrad1: Int = 8, // Card color
    cardGrad2: Int = 9,
    borderWidth: Dp = 1.dp,
    borderColor: Int = 18,
    buttonColor: Int = 10, // Button Color
    titleColor: Int = 1, // Title color
    textColor: Int = 2, // button text color
    iconColor: Int = 4,
    exitOperation: () -> Unit, // the exit button
    closeOperation: () -> Unit,
    menuPages: @Composable BoxScope.()-> Unit // the main menu pages
) {
    createCard(
        xOffset = menuOffset, yOffset = titleOffset,
        width = menuWidth - 55.dp, height = 50.dp, elevation,
        themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
        borderWidth = borderWidth, borderColor = borderColor,
        cardContent = {
            textRow(
                displayedText = menuTitle, textOffset = 10.dp,
                fontSize = 30.sp, font = FontWeight.Normal,
                themeColor = themeColor, textColor = titleColor
            )
        }
    )
    createCard(
        xOffset = (menuOffset + (menuWidth - 50.dp)), yOffset = titleOffset,
        width = 50.dp, height = 50.dp, elevation,
        themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
        borderWidth = borderWidth, borderColor = borderColor,
        cardContent = {
            IconButton(onClick = closeOperation) {
                Icon(
                    imageVector = Icons.Sharp.Close,
                    contentDescription = "Close Button",
                    tint = themeColor[iconColor]
                )
            }
        }
    )
    createCard(
        xOffset = menuOffset, yOffset = returnOffset,
        width = menuWidth, height = 50.dp, elevation = elevation,
        themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
        borderWidth = borderWidth, borderColor = borderColor,
        cardContent = {
            buttonRow(
                buttonOffset = 25.dp, width = 250.dp,
                buttonEvent = exitOperation,
                buttonText = returnTitle, themeColor = themeColor,
                buttonColor = buttonColor, textColor = textColor
            )
        }
    )
    createCard(
        xOffset = menuOffset, yOffset = mainOffset,
        width = menuWidth, height = mainHeight, elevation = elevation,
        themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
        borderWidth = borderWidth, borderColor = borderColor,
        cardContent = menuPages
    )
}