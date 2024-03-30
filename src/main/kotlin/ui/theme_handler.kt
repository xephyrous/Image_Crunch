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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

Also in development: Theme confirmation window :D
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
class ThemeSwitcher {
    var currentTheme by mutableStateOf<List<Color>?>(null)
    var newTheme by mutableStateOf<List<Color>?>(null)

    var displayed by mutableStateOf(false)

    @Composable
    fun createSwitcher(
        screenWidth: Dp,
        screenHeight: Dp,
        xScale: Float,
        yScale: Float,
        vm: ViewModel,
        themeColor: List<Color>,
        cardGrad1: Int = 8,
        cardGrad2: Int = 9,
        borderWidth: Dp = 1.dp,
        borderColor: Int = 18,
        buttonColor: Int = 10,
        textColor: Int = 2
    ) {
        AnimatedVisibility(
            visible = displayed,
            enter = scaleIn(),
            exit = scaleOut(),
        ){
            createCard(
                xOffset = screenWidth/4, yOffset = screenHeight/4, width = screenWidth/2, height = screenHeight/2,
                xScale = xScale, yScale = yScale, elevation = 50.dp, themeColor = themeColor,
                cardGrad1 = cardGrad1, cardGrad2 = cardGrad2, borderWidth = borderWidth, borderColor = borderColor,
                cardContent = {
                    Column {
                        textElement(
                            height = 350.dp, width = screenWidth/2, xScale = xScale, yScale = yScale,
                            displayedText = "CONFIRM THEME CHANGE?\n\nYou can always change this back later in the main menu",
                            textOffset = 40.dp, fontSize = 40.sp,
                            themeColor = themeColor, textColor = textColor
                        )
                        Row {
                            buttonElement(
                                height = 50.dp, width = 150.dp, buttonHeight = 40.dp, buttonWidth = 100.dp,
                                xScale = xScale, yScale = yScale, buttonText = "Confirm", themeColor = themeColor,
                                buttonColor = buttonColor, textColor = textColor,
                                buttonEvent = {
                                    confirmChange(vm)
                                }
                            )
                            textElement(
                                height = 50.dp, width = 300.dp, xScale = xScale, yScale = yScale,
                                displayedText = "filla text until countdown is made :D", textOffset = 10.dp, fontSize = 16.sp,
                                themeColor = themeColor, textColor = textColor
                            )
                            buttonElement(
                                height = 50.dp, width = 150.dp, buttonHeight = 40.dp, buttonWidth = 100.dp,
                                xScale = xScale, yScale = yScale, buttonText = "Reject", themeColor = themeColor,
                                buttonColor = buttonColor, textColor = textColor,
                                buttonEvent = {
                                    rejectChange(vm)
                                }
                            )
                        }
                    }
                }
            )
        }
    }

    fun initiateChange(
        currentColor: List<Color>,
        newColor: List<Color>,
        vm: ViewModel
    ) {
        displayed = true
        newTheme = newColor
        currentTheme = currentColor
        vm.themeColor = newColor
    }

    fun confirmChange(
        vm: ViewModel
    ) {
        vm.themeColor = newTheme!!

        newTheme = null
        currentTheme = null

        displayed = false
    }

    fun rejectChange(
        vm: ViewModel
    ) {
        vm.themeColor = currentTheme!!

        currentTheme = null
        newTheme = null

        displayed = false
    }
}