package ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Represents an Alert Box and its interaction functions
 */
class AlertBox {
    var text: String = ""
    var displayed by mutableStateOf(false)

    /**
     * Creates an Alert GUI object
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
    fun CreateAlert(
        screenWidth: Dp,
        screenHeight: Dp,
        xScale: Float,
        yScale: Float,
        themeColor: List<Color>,
        cardGrad1: Int = 8,
        cardGrad2: Int = 9,
        borderWidth: Dp = 1.dp,
        borderColor: Int = 18,
        iconColor: Int = 4,
        textColor: Int = 2
    ) {
        verticalVisibilityPane(
            visibility = displayed, animationHeight = 2, duration = 250, paneContent = {
                createCard(
                    xOffset = 5.dp, yOffset = screenHeight-60.dp,
                    width = screenWidth-65.dp, height = 50.dp,
                    xScale = xScale, yScale = yScale, elevation = 20.dp,
                    themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
                    borderWidth = borderWidth, borderColor = borderColor,
                    cardContent = {
                        textElement(
                            displayedText = text, textOffset = 15.dp, xScale = xScale, yScale = yScale,
                            fontSize = 18.sp, font = FontWeight.Normal, themeColor = themeColor, textColor = textColor
                        )
                    }
                )
                createCard(
                    xOffset = screenWidth-55.dp, yOffset = screenHeight-60.dp,
                    width = 50.dp, height = 50.dp,
                    xScale = xScale, yScale = yScale, elevation = 20.dp,
                    themeColor = themeColor, cardGrad1 = cardGrad1, cardGrad2 = cardGrad2,
                    borderWidth = borderWidth, borderColor = borderColor,
                    cardContent = {
                        IconButton(onClick = { displayed = false }) {
                            Icon(
                                imageVector = Icons.Sharp.Close,
                                contentDescription = "Close Button",
                                tint = themeColor[iconColor]
                            )
                        }
                    }
                )
            }
        )
    }

    /**
     * Displays an alert GUI object for a set amount of time
     * Uses [java.lang.Thread] for displaying
     *
     * @param displayedText The text to be displayed in the alert
     * @param displayTime The amount of time, in milliseconds, that the alert is displayed for
     */
    fun DisplayAlert(
        displayedText: String,
        displayTime: Long = 3000
    ) {
        Thread(Runnable {
            runBlocking {
                WaitForCondition(50000, 100)
            }
            this.text = displayedText
            this.displayed = true
            Timer().schedule(timerTask { displayed = false }, displayTime)
        }).start()
    }

    /**
     * Waits for a condition (If the alert is not displayed) to be met before returning
     * @see DisplayAlert For usage in thread closing
     *
     * @param maxDelay The longest the condition can be delayed before returning false
     * @param checkPeriod How often the condition is checked, a polling rate
     */
    tailrec suspend fun WaitForCondition(maxDelay: Long, checkPeriod: Long) : Boolean {
        if(maxDelay < 0) return false
        if(!displayed) return true
        delay(checkPeriod)
        return WaitForCondition(maxDelay - checkPeriod, checkPeriod)
    }
}