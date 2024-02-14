package ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Set of functions to make composable easier
// Passes inputs in a more user-friendly way when designing app

@Composable
fun buttonRow(
    rowOffset: Dp,
    buttonOffset: Dp,
    width: Dp,
    buttonEvent: () -> Unit,
    buttonText: String,
    themeColor: List<Color>,
    buttonColor: Int,
    textColor: Int,
){
    Row(
        modifier = Modifier.offset(y = rowOffset)
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
    rowOffset: Dp,
    displayedText: String,
    textOffset: Dp,
    fontSize: TextUnit,
    font: FontWeight,
    themeColor: List<Color>,
    textColor: Int
){
    Row(
        modifier = Modifier.offset(y = rowOffset)
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
    animationWidth: Int,
    duration: Int,
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
    animationHeight: Int,
    duration: Int,
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
    cardColor: Int,
    cardContent: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.offset(xOffset, yOffset).size(width, height),
        backgroundColor = themeColor[cardColor],
        elevation = elevation,
        content = cardContent
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
    cardColor: Int, // Card color
    buttonColor: Int, // Button Color
    titleColor: Int, // Title color
    textColor: Int, // button text color
    exitOperation: () -> Unit, // the exit button
    menuPages: @Composable () -> Unit // the main menu pages
) {
    createCard(
        xOffset = menuOffset, yOffset = titleOffset,
        width = menuWidth, height = 50.dp, elevation,
        themeColor = themeColor, cardColor = cardColor,
        cardContent = {
            textRow(
                rowOffset = 0.dp, displayedText = menuTitle, textOffset = 10.dp,
                fontSize = 30.sp, font = FontWeight.Normal,
                themeColor = themeColor, textColor = titleColor
            )
        }
    )
    createCard(
        xOffset = menuOffset, yOffset = returnOffset,
        width = menuWidth, height = 50.dp, elevation = elevation,
        themeColor = themeColor, cardColor = cardColor,
        cardContent = {
            buttonRow(
                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                buttonEvent = exitOperation,
                buttonText = returnTitle, themeColor = themeColor,
                buttonColor = buttonColor, textColor = textColor
            )
        }
    )
    createCard(
        xOffset = menuOffset, yOffset = mainOffset,
        width = menuWidth, height = mainHeight, elevation = elevation,
        themeColor = themeColor, cardColor = cardColor,
        cardContent = menuPages
    )
}