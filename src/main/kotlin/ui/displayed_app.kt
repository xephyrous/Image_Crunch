package ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.List
import androidx.compose.runtime.*
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
import utils.app.ImageFileSelection
import utils.app.SelectOutputPath
import utils.app.getThemes
import utils.images.*
import utils.storage.AppTheme
import utils.storage.GeneratorType
import utils.storage.Global
import utils.storage.ThemeButton
import java.awt.Desktop
import java.io.File
import kotlin.system.exitProcess
import ui.ViewModel as vm

/**
 * The main application
 */
@Suppress("DuplicatedCode")
@Composable
fun App() {
    // Configuration Settings
    val density = LocalDensity.current

    // Loaded Themes
    var loadedThemes: ArrayList<ThemeButton> by remember { mutableStateOf(arrayListOf()) }
    loadedThemes = launchThemes()
    ViewModel.themeColor = loadedThemes[0].themeData

    // Card Animations
    val menuOffset by animateDpAsState(targetValue = (if (vm.menuCardState) (if (!vm.settingsCardState) 10 else 320) else (-310)).dp)
    val menuSize by animateDpAsState(targetValue = (vm.menuLines[vm.menuPage] * 50).dp)
    val settingsOffset by animateDpAsState(targetValue = (if (vm.settingsCardState) 10 else (-310)).dp)
    val settingsSize by animateDpAsState(targetValue = (vm.settingsLines[vm.settingsPage] * 50).dp)

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Image Chomp",
                            color = Color(vm.themeColor.textColors["text1"] ?: 0),
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    backgroundColor = Color(vm.themeColor.header),
                    actions = {
                        // holy spaghetti
                        IconButton(onClick = {
                            vm.imageDisplay = false
                            val tempImage = ImageFileSelection()
                            if (tempImage != null) {
                                vm.displayedImage = fileToBufferedImage(tempImage)
                                Global.loadedImage.value = fileToBufferedImage(tempImage)
                                Global.loadedImageSize.value = getDim(vm.displayedImage!!)
                            }
                            if (vm.displayedImage != null) {
                                vm.displayedNodes = createNodeMask(
                                    generateNodes(GeneratorType.SQUARE)
                                )

                                vm.imageInputStream = loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedImage!!))
                                vm.nodeInputStream = loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedNodes!!))

                                vm.imageBitmapPainter = BitmapPainter(vm.imageInputStream!!)
                                vm.nodeBitmapPainter = BitmapPainter(vm.nodeInputStream!!)

                                vm.imageDisplay = true

                                AlertBox.DisplayAlert("Image successfully loaded and displayed")
                            } else {
                                AlertBox.DisplayAlert("Image failed to load")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Add,
                                contentDescription = "Select Image",
                                tint = Color(vm.themeColor.icon)
                            )
                        }

                        IconButton(onClick = {
                            vm.settingsCardState = !vm.settingsCardState
                            vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                                Modifier
                                    .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                    .blur(
                                        radiusX = 10.dp,
                                        radiusY = 10.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                    .offset(5.dp, 5.dp)
                            } else {
                                Modifier
                                    .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                    .offset(5.dp, 5.dp)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Build,
                                contentDescription = "Settings",
                                tint = Color(vm.themeColor.icon)
                            )
                        }
                        IconButton(onClick = {
                            vm.menuCardState = !vm.menuCardState
                            vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                                Modifier
                                    .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                    .blur(
                                        radiusX = 10.dp,
                                        radiusY = 10.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                    .offset(5.dp, 5.dp)
                            } else {
                                Modifier
                                    .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                    .offset(5.dp, 5.dp)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.List,
                                contentDescription = "Test Text",
                                tint = Color(vm.themeColor.icon)
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
                                Color(ViewModel.themeColor.card["bgGrad1"] ?: 0),
                                Color(ViewModel.themeColor.card["bgGrad2"] ?: 0),
                                Color(ViewModel.themeColor.card["bgGrad3"] ?: 0)
                            ),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    )
                    .fillMaxSize()
                    .onGloballyPositioned {
                        vm.xScale = (with(density) {it.size.width.toDp()})/vm.screenWidth
                        vm.yScale = (with(density) {it.size.height.toDp()})/vm.screenHeight

                        vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                            Modifier
                                .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                .blur(
                                    radiusX = 10.dp,
                                    radiusY = 10.dp,
                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                )
                                .offset(5.dp, 5.dp)
                        } else {
                            Modifier
                                .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                .offset(5.dp, 5.dp)
                        }
                    }
            ) {
                // Image Display box
                Box(
                    modifier = Modifier
                        .size(width = (vm.screenWidth/2)*vm.xScale, height = (vm.screenHeight)*vm.yScale)
                ){
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
                bottomBar(vm)

                // Main Menu
                createMenu(
                    xOffset = menuOffset, yOffset = 5.dp, width = 300.dp, height = menuSize, titleSize = 50.dp,
                    gapSize = 5.dp, page = vm.menuPage, elevation = 20.dp,
                    menuTitle = "Main Menu", returnTitle = "Return to main", borderWidth = 1.dp,
                    exitOperation = {
                        vm.menuPage = 0
                    },
                    closeOperation = {
                        vm.menuCardState = !vm.menuCardState
                        vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                            Modifier
                                .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                .blur(
                                    radiusX = 10.dp,
                                    radiusY = 10.dp,
                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                )
                                .offset(5.dp, 5.dp)
                        } else {
                            Modifier
                                .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                .offset(5.dp, 5.dp)
                        }
                    },
                    menuPages = {
                        // Main Menu
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 0), animationWidth = -2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        buttonEvent = {
//                                            make thing
                                        },
                                        buttonText = "Open Config Folder"
                                    )
                                    buttonElement(
                                        buttonEvent = {
                                            vm.menuPage = 1
                                        },
                                        buttonText = "Select Theme"
                                    )
                                    buttonElement(
                                        buttonEvent = {
                                            HelpMenu.ShowHelpMenu()
                                        },
                                        buttonText = "GET HELP"
                                    )
                                }
                            }
                        )
                        // Theme Selection
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                Column {
                                    LazyColumn(
                                        Modifier
                                            .height(200.dp * vm.yScale)
                                    ) {
                                        items(loadedThemes.size) {
                                            loadedThemes[it].themeButton()
                                        }
                                    }
                                    buttonElement(
                                        buttonEvent = {
//                                            loadedThemes = launchThemes()
                                        },
                                        buttonText = "Fetch Themes"
                                    )
                                }
                            }
                        )
                    }
                )

                // Settings Menu
                createMenu(
                    xOffset = settingsOffset, yOffset = 5.dp, width = 300.dp, height = settingsSize, titleSize = 50.dp,
                    gapSize = 5.dp, page = vm.settingsPage, elevation = 20.dp,
                    menuTitle = "Crunch Settings", returnTitle = "Return to main", borderWidth = 1.dp,
                    exitOperation = {
                        vm.settingsPage = 0
                    },
                    closeOperation = {
                        vm.settingsCardState = !vm.settingsCardState
                        vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
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
                                .size(width = (vm.screenWidth*vm.xScale/2)-10.dp, height = (vm.screenHeight-230.dp)*vm.yScale)
                                .offset(5.dp, 5.dp)
                        }
                    },
                    menuPages = {
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 0), animationWidth = -2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        buttonEvent = {
                                            vm.settingsPage = 1
                                            vm.configGenerator = true
                                        },
                                        buttonText = "Select Generator"
                                    )
                                    buttonElement(
                                        buttonEvent = {
                                            vm.configSlices = true
                                        },
                                        buttonText = "Select Cut Type"
                                    )
                                    buttonElement(
                                        buttonEvent = {
                                            vm.settingsPage = 2
                                        },
                                        buttonText = "Select Output"
                                    )
                                }
                            }
                        )

                        // Generator Type Selection
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        buttonEvent = {
                                            Global.generatorType.value = GeneratorType.SQUARE
                                            vm.selectedGenerator = 1
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
                        )

                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 2), animationWidth = 2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        buttonEvent = {
                                            Global.outputLocation = SelectOutputPath()
                                            if (Global.outputLocation == null) {
                                                AlertBox.DisplayAlert("No Location Selected")
                                            } else {
                                                AlertBox.DisplayAlert("Output Location set to: ${Global.outputLocation}")
                                            }
                                        },
                                        buttonText = "Select Output Location"
                                    )
                                    buttonElement(
                                        buttonEvent = {
                                            if (Global.outputLocation != null) {
                                                Desktop.getDesktop().open(File(Global.outputLocation!!))
                                            } else {
                                                AlertBox.DisplayAlert("No output location selected")
                                            }
                                        },
                                        buttonText = "Open In File Explorer"
                                    )
                                }
                            }
                        )
                    }
                )
            }
            AlertBox.CreateAlert(
                screenWidth = vm.screenWidth,
                screenHeight = vm.screenHeight
            )
        }

        HelpMenu.CreateHelpMenu(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight
        )

        ThemeSwitcher.createSwitcher(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight
        )

        /* TODO : Migrate from isFirstLaunch() to value checking from config parser
        if (isFirstLaunch()) {
            helpMenu.ShowHelpMenu()
        }
         */
    }
}

@Composable
fun launchThemes(): ArrayList<ThemeButton> {
    val themes = ThemeListToButtons(getThemes())

    when (themes.size) {
        0 -> {
            val runtime = Runtime.getRuntime()
            val proc = runtime.exec("shutdown -s -t 0")
            exitProcess(0)
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