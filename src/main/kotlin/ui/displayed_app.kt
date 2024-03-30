package ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.List
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.app.ImageFileSelection
import utils.app.SelectOutputPath
import utils.images.*
import utils.storage.*
import java.awt.Desktop
import java.io.File

val alertsHandler = AlertBox()
val helpMenu = HelpMenu()
val themeSwitcher = ThemeSwitcher()

/**
 * The main application
 */
@Suppress("DuplicatedCode")
@Composable
fun App() {
    val vm = remember { ViewModel() }

    // Configuration Settings
    val density = LocalDensity.current
    
    // Card Animations
    val menuOffset by animateDpAsState(targetValue = (if (vm.menuCardState) (if (!vm.settingsCardState) 10 else 320) else (-310)).dp)
    val menuSize by animateDpAsState(targetValue = (vm.menuLines[vm.menuPage] * 50).dp)
    val settingsOffset by animateDpAsState(targetValue = (if (vm.settingsCardState) 10 else (-310)).dp)
    val settingsSize by animateDpAsState(targetValue = (vm.settingsLines[vm.settingsPage] * 50).dp)
    val fabOffset by animateDpAsState(targetValue = if (vm.configSlices) 50.dp else 0.dp)

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Image Chomp",
                            color = vm.themeColor[1],
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    backgroundColor = vm.themeColor[0],
                    actions = {
                        // holy spaghetti
                        IconButton(onClick = {
                            vm.imageDisplay = false
                            val tempImage = ImageFileSelection()
                            if (tempImage != null) {
                                vm.displayedImage = fileToBufferedImage(tempImage)
                                loadedImage.set(fileToBufferedImage(tempImage))
                                loadedImageSize.set(getDim(vm.displayedImage!!))
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

                                alertsHandler.DisplayAlert("Image successfully loaded and displayed")
                            } else {
                                alertsHandler.DisplayAlert("Image failed to load")
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Add,
                                contentDescription = "Select Image",
                                tint = vm.themeColor[4]
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
                                tint = vm.themeColor[4]
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
                                tint = vm.themeColor[4]
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        if (loadedImage.value() == null) {
                            alertsHandler.DisplayAlert("Please select an image", 3002)
                        } else if (outputLocation == null) {
                            alertsHandler.DisplayAlert("Please select an output location", 3001)
                        } else {
                            loadedImage.lock()
                            loadedImageSize.lock()
                            squareRows.lock()
                            squareColumns.lock()
                            generatorType.lock()
                            nodes.lock()
                            mask.lock()

                            slices.set(runImagePipeline(generatorType.value()))

                            slices.lock()

                            for (i in slices.value()!!.indices) {
                                maskToImage(loadedImage.value()!!, slices.value()!![i], "Output-${i}")
                            }

                            loadedImage.unlock()
                            loadedImageSize.unlock()
                            squareRows.unlock()
                            squareColumns.unlock()
                            generatorType.unlock()
                            nodes.unlock()
                            mask.unlock()
                            slices.unlock()
                        }
                    },
                    text = { Text("Run", color = vm.themeColor[2], fontSize = 16.sp*vm.xScale.coerceAtMost(vm.yScale)) },
                    icon = {
                        Icon(
                            Icons.Sharp.PlayArrow, "Run",
                            tint = vm.themeColor[4],
                            modifier = Modifier.size(25.dp*vm.xScale.coerceAtMost(vm.yScale), 25.dp*vm.xScale.coerceAtMost(vm.yScale))
                        )
                    },
                    backgroundColor = vm.themeColor[11],
                    modifier = Modifier
                        .offset(0.dp, (((-100).dp - fabOffset)*vm.yScale))
                        .size(width = 100.dp*vm.xScale, height = 50.dp*vm.yScale)
                )
            },
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(vm.themeColor[5], vm.themeColor[6], vm.themeColor[7]),
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

                // Bottom Bar
                createCard(
                    xOffset = 0.dp, yOffset = (vm.screenHeight-100.dp),
                    xScale = vm.xScale, yScale = vm.yScale,
                    width = vm.screenWidth/3, height = (400.dp), elevation = 5.dp,
                    themeColor = vm.themeColor, borderWidth = 1.dp,
                    cardContent = {
                        textElement(
                            height = 400.dp, width = vm.screenWidth/3, displayedText = "Node Generator\nSettings", textOffset = (15.dp),
                            xScale = vm.xScale, yScale = vm.yScale,
                            fontSize = 38.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 3
                        )
                    }
                )

                createCard(
                    xOffset = vm.screenWidth/3, vm.screenHeight-100.dp,
                    width = vm.screenWidth/3, height = 400.dp,
                    xScale = vm.xScale, yScale = vm.yScale,elevation = 5.dp,
                    themeColor = vm.themeColor, borderWidth = 1.dp,
                    cardContent = {
                        textElement(
                            height = 400.dp, width = vm.screenWidth/3, displayedText = "Mask Generator\nSettings", textOffset = 15.dp,
                            xScale = vm.xScale, yScale = vm.yScale,
                            fontSize = 38.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 3
                        )
                    }
                )

                createCard(
                    xOffset = vm.screenWidth/3*2, yOffset = vm.screenHeight-100.dp,
                    width = vm.screenWidth/3, height = 400.dp,
                    xScale = vm.xScale, yScale = vm.yScale, elevation = 5.dp,
                    themeColor = vm.themeColor, borderWidth = 1.dp,
                    cardContent = {
                        textElement(
                            height = 400.dp, width = vm.screenWidth/3, displayedText = "Slice Generator\nSettings",
                            xScale = vm.xScale, yScale = vm.yScale, textOffset = 15.dp,
                            fontSize = 38.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 3
                        )
                    }
                )

                // Bottom Bar Settings - From bottom_bar_selection.kt
                bottomBar(vm)

                // Main Menu
                createMenu(
                    xOffset = menuOffset, yOffset = 5.dp, width = 300.dp, height = menuSize, titleSize = 50.dp, gapSize = 5.dp,
                    xScale = vm.xScale, yScale = vm.yScale, page = vm.menuPage,elevation = 20.dp,
                    menuTitle = "Main Menu", returnTitle = "Return to main",
                    themeColor = vm.themeColor, borderWidth = 1.dp,
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
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
//                                            make thing
                                        },
                                        buttonText = "Open Config Folder", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            vm.menuPage = 1
                                        },
                                        buttonText = "Select Theme", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            helpMenu.ShowHelpMenu()
                                        },
                                        buttonText = "GET HELP", themeColor = vm.themeColor
                                    )
                                }
                            }
                        )
                        // Theme Selection
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                // Convert this and make it a scrollable list if more than some amount is present
                                Column {
                                    Column (
                                        Modifier
                                            .height(200.dp*vm.yScale)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {
                                                themeSwitcher.initiateChange(
                                                    vm.themeColor, darkThemes, vm
                                                )
                                            },
                                            buttonText = "Theme: Dark", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {
                                                themeSwitcher.initiateChange(
                                                    vm.themeColor, lightThemes, vm
                                                )
                                            },
                                            buttonText = "Theme: Light", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {
                                                themeSwitcher.initiateChange(
                                                    vm.themeColor, celesteThemes, vm
                                                )
                                            },
                                            buttonText = "Theme: Celeste", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {
                                                themeSwitcher.initiateChange(
                                                    vm.themeColor, aqueousThemes, vm
                                                )
                                            },
                                            buttonText = "Theme: Aqueous", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {

                                            },
                                            buttonText = "Theme: FILLER", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {

                                            },
                                            buttonText = "Theme: FILLER", themeColor = vm.themeColor
                                        )
                                        buttonElement(
                                            xScale = vm.xScale, yScale = vm.yScale,
                                            buttonEvent = {

                                            },
                                            buttonText = "Theme: FILLER", themeColor = vm.themeColor
                                        )
                                    }
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            TODO("Fetch Themes")
                                        },
                                        buttonText = "Fetch Themes", themeColor = vm.themeColor
                                    )
                                }
                            }
                        )
                    }
                )

                // Settings Menu
                createMenu(
                    xOffset = settingsOffset, yOffset = 5.dp, width = 300.dp, height = settingsSize, titleSize = 50.dp, gapSize = 5.dp,
                    xScale = vm.xScale, yScale = vm.yScale, page = vm.settingsPage, elevation = 20.dp,
                    menuTitle = "Crunch Settings", returnTitle = "Return to main",
                    themeColor = vm.themeColor, borderWidth = 1.dp,
                    exitOperation = {
                        vm.settingsPage = 0
                    },
                    closeOperation = {
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
                    },
                    menuPages = {
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 0), animationWidth = -2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            vm.settingsPage = 1
                                            vm.configGenerator = true
                                        },
                                        buttonText = "Select Generator", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            vm.configSlices = true
                                        },
                                        buttonText = "Select Cut Type", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            vm.settingsPage = 2
                                        },
                                        buttonText = "Select Output", themeColor = vm.themeColor
                                    )
                                }
                            }
                        )

                        // Generator Type Selection
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            generatorType.set(GeneratorType.SQUARE)
                                            vm.selectedGenerator = 1
                                        },
                                        buttonText = "Square Generator", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {

                                        },
                                        buttonText = "Does Not Exist",  themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {

                                        },
                                        buttonText = "Does Not Exist", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {

                                        },
                                        buttonText = "Does Not Exist", themeColor = vm.themeColor
                                    )
                                }
                            }
                        )

                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 2), animationWidth = 2, duration = 369, paneContent = {
                                Column {
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            outputLocation = SelectOutputPath()
                                            if (outputLocation == null) {
                                                alertsHandler.DisplayAlert("No Location Selected")
                                            } else {
                                                alertsHandler.DisplayAlert("Output Location set to: $outputLocation")
                                            }
                                        },
                                        buttonText = "Select Output Location", themeColor = vm.themeColor
                                    )
                                    buttonElement(
                                        xScale = vm.xScale, yScale = vm.yScale,
                                        buttonEvent = {
                                            if (outputLocation != null) {
                                                Desktop.getDesktop().open(File(outputLocation!!))
                                            } else {
                                                alertsHandler.DisplayAlert("No output location selected")
                                            }
                                        },
                                        buttonText = "Open In File Explorer", themeColor = vm.themeColor
                                    )
                                }
                            }
                        )
                    }
                )
            }
            alertsHandler.CreateAlert(
                screenWidth = vm.screenWidth, screenHeight = vm.screenHeight, xScale = vm.xScale, yScale = vm.yScale, themeColor = vm.themeColor
            )
        }

        helpMenu.CreateHelpMenu(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight, xScale = vm.xScale, yScale = vm.yScale, themeColor = vm.themeColor
        )

        themeSwitcher.createSwitcher(
            screenWidth = vm.screenWidth, screenHeight = vm.screenHeight, xScale = vm.xScale, yScale = vm.yScale,
            vm = vm, themeColor = vm.themeColor
        )

        /* TODO : Migrate from isFirstLaunch() to value checking from config parser
        if (isFirstLaunch()) {
            helpMenu.ShowHelpMenu()
        }
         */
    }
}