package ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ui.ViewModel
import utils.app.getThemes
import utils.app.imageFileSelection
import utils.app.selectOutputPath
import utils.images.*
import utils.storage.AppTheme
import utils.storage.GeneratorType
import utils.storage.Global
import utils.storage.ThemeButton
import java.awt.Desktop
import java.io.File
import java.nio.file.Paths
import ui.ViewModel as vm

/**
 * The main application
 */
@Suppress("DuplicatedCode")
@Composable
fun app() {
    // Configuration Settings
    val density = LocalDensity.current

    // me n u
    val appMenu = SettingsMenu(
        listOf(
            MenuButton(
                pageTitle = "Open Config",
                buttonEvent = {
                    Desktop.getDesktop().open(
                        File(
                            Paths.get("").toAbsolutePath().toString() + "\\config\\"
                        )
                    )
                }
            ),
            MenuPage(
                pageTitle = "Select Theme",
                pageSize = 6,
                menuPage = {
                    Column {
                        LazyColumn(
                            Modifier
                                .height(200.dp * vm.yScale)
                        ) {
                            items(ViewModel.loadedThemes.size) { item ->
                                buttonElement(
                                    buttonText = "Theme ${item + 1}: ${ViewModel.loadedThemes[item].themeData.name}",
                                    height = ViewModel.loadedThemes[item].height,
                                    buttonHeight = ViewModel.loadedThemes[item].height - 10.dp,
                                    buttonEvent = {
                                        ThemeSwitcher.initiateChange(ViewModel.loadedThemes[item].themeData)
                                    }
                                )
                            }
                        }
                        buttonElement(
                            buttonEvent = {
                                Desktop.getDesktop().open(
                                    File(
                                        Paths.get("").toAbsolutePath().toString() + "\\config\\themes\\"
                                    )
                                )
                            },
                            buttonText = "Open Themes Folder"
                        )
                        buttonElement(
                            buttonEvent = {
                                ViewModel.loadedThemes = launchThemes()
                            },
                            buttonText = "Fetch Themes"
                        )
                    }
                }
            ),
            MenuPage(
                pageTitle = "test menu",
                pageSize = 2,
                menuPage = {
                    Column {
                        buttonElement(
                            buttonEvent = {
                                Desktop.getDesktop().open(
                                    File(
                                        Paths.get("").toAbsolutePath().toString() + "\\config\\themes\\"
                                    )
                                )
                            },
                            buttonText = "Open Themes Folder"
                        )
                        buttonElement(
                            buttonEvent = {
                                Desktop.getDesktop().open(
                                    File(
                                        Paths.get("").toAbsolutePath().toString() + "\\config\\themes\\"
                                    )
                                )
                            },
                            buttonText = "Open Themes Folder"
                        )
                    }
                }
            ),
            MenuButton(
                pageTitle = "View Help Menu",
                buttonEvent = {
                    HelpMenu.showHelpMenu()
                }
            )
        ),
        "App Settings"
    )

    val genMenu = SettingsMenu(
        listOf(
            MenuPage(
                pageTitle = "Add Generator",
                pageSize = 4,
                menuPage = {
                    Column {
                        buttonElement(
                            buttonEvent = {
                                Global.generatorType.value = GeneratorType.SQUARE
                            },
                            buttonText = "Square Generator"
                        )
                        buttonElement(
                            buttonEvent = {

                            },
                            buttonText = "Does Not Exist"
                        )
                        buttonElement(
                            buttonEvent = {

                            },
                            buttonText = "Does Not Exist"
                        )
                        buttonElement(
                            buttonEvent = {

                            },
                            buttonText = "Does Not Exist"
                        )
                    }
                }
            ),
            MenuPage(
                pageTitle = "Add Cut Type",
                pageSize = 1,
                menuPage = {}
            ),
            MenuPage(
                pageTitle = "Select Output",
                pageSize = 2,
                menuPage = {
                    Column {
                        buttonElement(
                            buttonEvent = {
                                Global.outputLocation = selectOutputPath()
                                if (Global.outputLocation == null) {
                                    AlertBox.displayAlert("No Location Selected")
                                } else {
                                    AlertBox.displayAlert("Output Location set to: ${Global.outputLocation}")
                                }
                            },
                            buttonText = "Select Output Location"
                        )
                        buttonElement(
                            buttonEvent = {
                                if (Global.outputLocation != null) {
                                    Desktop.getDesktop().open(File(Global.outputLocation!!))
                                } else {
                                    AlertBox.displayAlert("No output location selected")
                                }
                            },
                            buttonText = "Open In File Explorer"
                        )
                    }
                }
            )
        ),
        "Gen. Settings"
    )

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Image Chomp",
                            color = vm.themeColor.textColors["text1"] ?: Color(0),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    backgroundColor = vm.themeColor.header,
                    actions = {
                        // holy spaghetti
                        // TODO: some idiot needs to look at this and see if there is a better way to load this image properly because 4:36 am coding is not ideal for writing an efficient image-loader
                        IconButton(onClick = {
                            vm.imageDisplay = false
                            val tempImage = imageFileSelection()
                            if (tempImage != null) {
                                vm.displayedImage = fileToBufferedImage(tempImage)
                                Global.loadedImage.value = fileToBufferedImage(tempImage)
                                Global.loadedImageSize.value = getDim(vm.displayedImage!!)
                            }
                            if (vm.displayedImage != null) {
                                vm.displayedNodes = createNodeMask(
                                    generateNodes(GeneratorType.SQUARE)
                                )

                                vm.imageInputStream =
                                    loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedImage!!))
                                vm.nodeInputStream =
                                    loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedNodes!!))

                                vm.imageBitmapPainter = BitmapPainter(vm.imageInputStream!!)
                                vm.nodeBitmapPainter = BitmapPainter(vm.nodeInputStream!!)

                                vm.imageDisplay = true

                                AlertBox.displayAlert("Image successfully loaded and displayed")
                            } else {
                                AlertBox.displayAlert("Image failed to load")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Add,
                                contentDescription = "Select Image",
                                tint = vm.themeColor.icon
                            )
                        }

                        IconButton(onClick = {
                            if (genMenu.isOpen) {
                                genMenu.closeMenu()
                            } else {
                                genMenu.openMenu()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Build,
                                contentDescription = "Settings",
                                tint = vm.themeColor.icon
                            )
                        }
                        IconButton(onClick = {
                            if (appMenu.isOpen) {
                                appMenu.closeMenu()
                            } else {
                                appMenu.openMenu()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.List,
                                contentDescription = "Test Text",
                                tint = vm.themeColor.icon
                            )
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                ViewModel.themeColor.backgroundColors["bgGrad1"] ?: Color(0),
                                ViewModel.themeColor.backgroundColors["bgGrad2"] ?: Color(0),
                                ViewModel.themeColor.backgroundColors["bgGrad3"] ?: Color(0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    )
                    .fillMaxSize()
                    .onGloballyPositioned {
                        vm.xScale = (with(density) { it.size.width.toDp() }) / vm.screenWidth
                        vm.yScale = (with(density) { it.size.height.toDp() }) / vm.screenHeight

                        vm.imageModifier = if (appMenu.isOpen || genMenu.isOpen) {
                            Modifier
                                .size(
                                    width = (vm.screenWidth * vm.xScale / 2) - 10.dp,
                                    height = (vm.screenHeight - 230.dp) * vm.yScale
                                )
                                .blur(
                                    radiusX = 10.dp,
                                    radiusY = 10.dp,
                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                )
                                .offset(5.dp, 5.dp)
                        } else {
                            Modifier
                                .size(
                                    width = (vm.screenWidth * vm.xScale / 2) - 10.dp,
                                    height = (vm.screenHeight - 230.dp) * vm.yScale
                                )
                                .offset(5.dp, 5.dp)
                        }
                    }
            ) {
                // Image Display box
                Box(
                    modifier = Modifier
                        .size(width = (vm.screenWidth / 2) * vm.xScale, height = (vm.screenHeight) * vm.yScale)
                ) {
                    // IMAGE SCOPE
                    if (vm.imageDisplay) {
                        Image(
                            painter = vm.imageBitmapPainter!!,
                            contentDescription = "picture",
                            modifier = vm.imageModifier,
                            contentScale = ContentScale.Fit
                        )
                        if (vm.nodeDisplay) {
                            Image(
                                painter = vm.nodeBitmapPainter!!,
                                contentDescription = "picture",
                                modifier = vm.imageModifier,
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                }

                // Bottom Bar Settings - From bottom_bar_selection.kt
                BottomBar.createBottomBar()

                // Main Menu
                appMenu.createMenu()

                // Settings Menu
                genMenu.createMenu()
            }
            AlertBox.createAlert(
                screenWidth = vm.screenWidth,
                screenHeight = vm.screenHeight
            )
        }

        HelpMenu.createHelpMenu(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight
        )

        ThemeSwitcher.createSwitcher(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight
        )
    }
}

fun launchThemes(): ArrayList<ThemeButton> {
    val themes = themeListToButtons(getThemes())

    when (themes.size) {
        0 -> {
            AlertBox.displayAlert("Error Finding Themes, This could be caused by no themes being available to read")
        }

        1 -> {
            themes[0].setButtonHeight(200.dp)
        }

        2 -> {
            themes[0].setButtonHeight(100.dp)
            themes[1].setButtonHeight(100.dp)
        }

        3 -> {
            themes[0].setButtonHeight((200 / 3).dp)
            themes[1].setButtonHeight((200 / 3).dp)
            themes[2].setButtonHeight((200 / 3).dp)
        }
    }
    return themes
}

/*
      _
       \`*-.
        )  _`-.
       /  : `. .
      :  _   '  \
      ;  *` _.   `*-._
       `-.-'          `-.
         ;       `       `.
         :.       .        \
         . \  .   :   .-'   .
         '  `+.;  ;  '      :
         :  '  |    ;       ;-.
         ; '   : :`-:     _.`* ;
      .*' /  .*' ; .*`- +'  `*'
      `*-*   `*-*  `*-*'
 */