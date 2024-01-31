package ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun buttonRow(
    rowOffset: Dp,
    buttonOffset: Dp,
    width: Dp,
    buttonEvent: () -> Unit,
    buttonText: String,
    themeColor: List<Color>
){
    Row(
        modifier = Modifier.offset(y = rowOffset)
    ) {
        Button(
            onClick = buttonEvent,
            modifier = Modifier.offset(buttonOffset, 0.dp).width(width),
            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
        ) {
            Text(buttonText, color = themeColor[2])
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