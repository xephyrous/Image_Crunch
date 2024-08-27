package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import utils.storage.ThemeButton
import utils.storage.ThemeData
import java.io.File

/*
THEME HANDLER TODO:

Parse thru a folder list of themes
Convert Theme to a new object
Confirmation box

 */

/*
Hola from the code desk,

Please if possible make some checker that returns a list of theme objects
I will use that to determine the selections for the theme page
 */

/**
 * Grabs themes from the dedicated themes folder within config.
 * Returns a list of themes
 */
fun grabThemes(
    dirPath: String
): List<File>? {
    return File(dirPath).listFiles()?.filter { it.isFile } // i think this works?
}

/**
 * Represents the theme switcher and confirmation boxes
 */
object ThemeSwitcher {
    private var currentTheme by mutableStateOf(ThemeData(""))
    private var newTheme by mutableStateOf(ThemeData(""))

    private var displayed by mutableStateOf(false)

    private var countdown by mutableStateOf(15)

    @Composable
    fun createSwitcher(
        screenWidth: Dp,
        screenHeight: Dp,
        borderWidth: Dp = 1.dp,
    ) {
        AnimatedVisibility(
            visible = displayed,
            enter = scaleIn(
                transformOrigin = TransformOrigin.Center
            ),
            exit = scaleOut(
                transformOrigin = TransformOrigin.Center
            ),
        ){
            createCard(
                xOffset = screenWidth / 4, yOffset = screenHeight / 4, width = screenWidth / 2,
                height = screenHeight / 2, elevation = 50.dp, borderWidth = borderWidth,
                cardContent = {
                    Column {
                        textElement(
                            height = 325.dp, width = screenWidth/2,
                            displayedText = "CONFIRM THEME CHANGE?\n\nYou can always change this back later in the main menu",
                            textOffset = 40.dp, fontSize = 40.sp,
                        )
                        Row {
                            buttonElement(
                                height = 50.dp, width = 150.dp, buttonHeight = 40.dp,
                                buttonWidth = 100.dp, buttonText = "Confirm",
                                buttonEvent = {
                                    confirmChange()
                                }
                            )
                            textElement(
                                height = 50.dp, width = 300.dp,
                                displayedText = "Reverting Change in: $countdown", textOffset = 10.dp, fontSize = 16.sp,
                            )
                            buttonElement(
                                height = 50.dp, width = 150.dp, buttonHeight = 40.dp,
                                buttonWidth = 100.dp, buttonText = "Reject",
                                buttonEvent = {
                                    rejectChange()
                                }
                            )
                        }
                    }
                }
            )
        }
    }

    fun initiateChange(
        newColor: ThemeData
    ) {
        displayed = true
        currentTheme = ViewModel.themeColor
        ViewModel.themeColor = newColor
        startCountdown()
    }

    private fun confirmChange() {
        ViewModel.themeColor = newTheme

        newTheme = ThemeData("")
        currentTheme = ThemeData("")

        displayed = false
    }

    private fun rejectChange() {
        ViewModel.themeColor = currentTheme

        currentTheme = ThemeData("")
        newTheme = ThemeData("")

        displayed = false
    }

    private fun startCountdown() {
        Thread(kotlinx.coroutines.Runnable {
            countdown = 15
            while (countdown > 0 && displayed) {
                runBlocking {
                    delay(1000)
                }
                countdown--
            }
            if (displayed) {
                rejectChange()
            }
        }).start()
    }
}

@Composable
fun ThemeListToButtons(themes: ArrayList<ThemeData>): ArrayList<ThemeButton> {
    val themeButtons: ArrayList<ThemeButton> = arrayListOf()
    for (theme in themes) {
        themeButtons.add(ThemeButton(theme))
    }
    return themeButtons
}