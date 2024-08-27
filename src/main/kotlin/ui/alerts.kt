package ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.*
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Represents an Alert Box and its interaction functions
 */
object AlertBox {
    var text: String = ""
    var displayed by mutableStateOf(false)

    /**
     * Creates an Alert GUI object
     * @param screenWidth The screen width in dot points (Dp)
     * @param screenHeight The screen height in dot points (Dp)
     * @param borderWidth The width of the card border
     */
    @Composable
    fun CreateAlert(
        screenWidth: Dp,
        screenHeight: Dp,
        borderWidth: Dp = 1.dp,
    ) {
        verticalVisibilityPane(
            visibility = displayed, animationHeight = 2, duration = 250, paneContent = {
                createCard(
                    xOffset = 5.dp, yOffset = screenHeight - 60.dp, width = screenWidth - 65.dp, height = 50.dp,
                    elevation = 20.dp, borderWidth = borderWidth,
                    cardContent = {
                        textElement(
                            displayedText = text, textOffset = 15.dp, width = screenWidth - 65.dp,
                            fontSize = 18.sp, font = FontWeight.Normal
                        )
                    }
                )
                createCard(
                    xOffset = screenWidth - 55.dp, yOffset = screenHeight - 60.dp,
                    width = 50.dp, height = 50.dp, elevation = 20.dp,
                    borderWidth = borderWidth,
                    cardContent = {
                        IconButton(onClick = { displayed = false }, modifier = Modifier.align(Alignment.Center)) {
                            Icon(
                                imageVector = Icons.Sharp.Close,
                                contentDescription = "Close Button",
                                tint = Color(ViewModel.themeColor.icon)
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
        CoroutineScope(Dispatchers.Main).launch {
            // Use a suspend function for waiting condition
            waitForCondition(50000, 100)

            // Update the UI
            this@AlertBox.text = displayedText
            this@AlertBox.displayed = true

            // Schedule hiding the alert after a delay
            withContext(Dispatchers.IO) {
                Timer().schedule(timerTask {
                    // Switch back to the main thread to update the UI
                    CoroutineScope(Dispatchers.Main).launch {
                        this@AlertBox.displayed = false
                    }
                }, displayTime)
            }
        }
    }

    /**
     * Suspend function for waiting condition
     * TODO : Document function
     */
    private suspend fun waitForCondition(maxDelay: Long, checkPeriod: Long): Boolean {
        if (maxDelay < 0) return false
        if (!this@AlertBox.displayed) return true
        delay(checkPeriod)
        return waitForCondition(maxDelay - checkPeriod, checkPeriod)
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