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
import utils.storage.ThemeStorage

/**
 * Represents the theme switcher and confirmation boxes
 */
object ThemeSwitcher {
    private var currentTheme by mutableStateOf(ThemeStorage(ThemeData("")))
    private var newTheme by mutableStateOf(ThemeStorage(ThemeData("")))

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
        ) {
            createCard(
                xOffset = screenWidth / 4, yOffset = screenHeight / 4, width = screenWidth / 2,
                height = screenHeight / 2, elevation = 50.dp, borderWidth = borderWidth,
                cardContent = {
                    Column {
                        textElement(
                            height = 325.dp, width = screenWidth / 2,
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
        newTheme = ThemeStorage(newColor)
        println("Current theme: ${currentTheme.name} | New theme: ${newTheme.name}")
        ViewModel.themeColor = newTheme
        startCountdown()
    }

    private fun confirmChange() {
        ViewModel.themeColor = newTheme

        newTheme = ThemeStorage(ThemeData(""))
        currentTheme = ThemeStorage(ThemeData(""))

        displayed = false

        println("Current theme: ${ViewModel.themeColor.name}")
        println("Card: ${ViewModel.themeColor.card["cGrad1"]}")
    }

    private fun rejectChange() {
        ViewModel.themeColor = currentTheme

        currentTheme = ThemeStorage(ThemeData(""))
        newTheme = ThemeStorage(ThemeData(""))

        displayed = false

        println("Current theme: ${ViewModel.themeColor.name}")
    }

    private fun startCountdown() {
        Thread(kotlinx.coroutines.Runnable {
            countdown = 15
            while (countdown > 0 && displayed) {
                println(countdown.toString())
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

fun themeListToButtons(themes: ArrayList<ThemeData>): ArrayList<ThemeButton> {
    val themeButtons: ArrayList<ThemeButton> = arrayListOf()
    for (theme in themes) {
        themeButtons.add(ThemeButton(theme))
    }
    return themeButtons
}

/*         __..--''``---....___   _..._    __
 /// //_.-'    .-/";  `        ``<._  ``.''_ `. / // /
///_.-' _..--.'_    \                    `( ) ) // //
/ (_..-' // (< _     ;_..__               ; `' / ///
 / // // //  `-._,_)' // / ``--...____..-' /// / */