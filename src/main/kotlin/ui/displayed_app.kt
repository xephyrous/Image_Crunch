package ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
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
import utils.app.settingsToCSV
import utils.app.settingsToString
import utils.images.*
import utils.storage.*
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

@Suppress("DuplicatedCode")
@Composable
fun App() {
    val vm = remember { ViewModel() }

    // Configuration Settings
    val density = LocalDensity.current
    
    // Card Animations
    // TODO: rewrite this later to be more compact
    val menuOffset by animateDpAsState(targetValue = (if (vm.menuCardState) (if (!vm.settingsCardState) 10 else 320) else (-310)).dp)
    val menuSize by animateDpAsState(targetValue = (vm.menuLines * 50).dp)
    val menuTitle by animateDpAsState(targetValue = if (vm.menuCardState) 5.dp else 60.dp)
    val menuExit by animateDpAsState(targetValue = if (vm.menuPage == 0) (60 + ((vm.menuLines - 1) * 50)).dp else (65 + (vm.menuLines * 50)).dp)
    val settingsOffset by animateDpAsState(targetValue = (if (vm.settingsCardState) 10 else (-310)).dp)
    val settingsSize by animateDpAsState(targetValue = (vm.settingsLines * 50).dp)
    val settingsTitle by animateDpAsState(targetValue = if (vm.settingsCardState) 5.dp else 60.dp)
    val settingsExit by animateDpAsState(targetValue = if (vm.settingsPage == 0) (60 + ((vm.settingsLines - 1) * 50)).dp else (65 + (vm.settingsLines * 50)).dp)
    val bottomCardsY by animateDpAsState(targetValue = vm.screenHeight)
    val bottomCardsX by animateDpAsState(targetValue = vm.screenWidth)
    val fabLocation by animateDpAsState(targetValue = 0.dp)

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Image Chomp",
                            color = vm.themeColor[2],
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    backgroundColor = vm.themeColor[0],
                    actions = {
                        // holy spaghetti
                        IconButton(onClick = {
                            vm.displayed = false
                            val tempImage = ImageFileSelection()
                            if (tempImage != null) {
                                vm.displayedImage = fileToBufferedImage(tempImage)
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

                                vm.displayed = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Add,
                                contentDescription = "Select Image",
                                tint = vm.themeColor[3]
                            )
                        }

                        IconButton(onClick = {
                            vm.settingsCardState = !vm.settingsCardState
                            vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                                Modifier
                                    .size(width = (vm.screenWidth/2)-10.dp, height = vm.screenHeight-230.dp)
                                    .blur(
                                        radiusX = 10.dp,
                                        radiusY = 10.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                    .offset(5.dp, 5.dp)
                            } else {
                                Modifier.size(
                                    width = (vm.screenWidth/2)-10.dp,
                                    height = vm.screenHeight-230.dp
                                ).offset(5.dp, 5.dp)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Build,
                                contentDescription = "Settings",
                                tint = vm.themeColor[3]
                            )
                        }
                        IconButton(onClick = {
                            vm.menuCardState = !vm.menuCardState
                            vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                                Modifier
                                    .size(width = (vm.screenWidth/2)-10.dp, height = vm.screenHeight-230.dp)
                                    .blur(
                                        radiusX = 10.dp,
                                        radiusY = 10.dp,
                                        edgeTreatment = BlurredEdgeTreatment.Unbounded
                                    )
                                    .offset(5.dp, 5.dp)
                            } else {
                                Modifier.size(
                                    width = (vm.screenWidth/2)-10.dp,
                                    height = vm.screenHeight-230.dp
                                ).offset(5.dp, 5.dp)
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.List,
                                contentDescription = "Test Text",
                                tint = vm.themeColor[3]
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        // run the code :thumb:
                    },
                    text = { Text("Run", color = vm.themeColor[2]) },
                    icon = {
                        Icon(
                            Icons.Sharp.PlayArrow, "Run",
                            tint = vm.themeColor[3]
                        )
                    },
                    backgroundColor = vm.themeColor[4],
                    modifier = Modifier.offset(0.dp, (-100).dp)
                )
            },
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(vm.themeColor[1], vm.themeColor[5]),
                            start = Offset(0f, 0f),
                            end = Offset(Float.POSITIVE_INFINITY, 0f),
                            tileMode = TileMode.Clamp
                        )
                    )
                    .fillMaxSize()
                    .onGloballyPositioned {
                        vm.screenWidth = with(density) {it.size.width.toDp()}
                        vm.screenHeight = with(density) {it.size.height.toDp()}

                        vm.imageModifier = if (vm.menuCardState || vm.settingsCardState) {
                            Modifier
                                .size(width = (vm.screenWidth/2)-10.dp, height = vm.screenHeight-230.dp)
                                .blur(
                                    radiusX = 10.dp,
                                    radiusY = 10.dp,
                                    edgeTreatment = BlurredEdgeTreatment.Unbounded
                                )
                                .offset(5.dp, 5.dp)
                        } else {
                            Modifier.size(
                                width = (vm.screenWidth/2)-10.dp,
                                height = vm.screenHeight-230.dp
                            ).offset(5.dp, 5.dp)
                        }
                    }
            ) {
                // Display box
                Box(
                    modifier = Modifier
                        .size(width = vm.screenWidth/2, height = vm.screenHeight)
                ){
                    // IMAGE SCOPE
                    if (vm.displayed) {
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
                    xOffset = 0.dp, yOffset = bottomCardsY-100.dp,
                    width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                    themeColor = vm.themeColor, cardColor = 5,
                    cardContent = {
                        textRow(
                            rowOffset = 0.dp, displayedText = "Node Generator\nSettings", textOffset = 15.dp,
                            fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 2
                        )
                    }
                )

                createCard(
                    xOffset = bottomCardsX/3, yOffset = bottomCardsY-100.dp,
                    width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                    themeColor = vm.themeColor, cardColor = 5,
                    cardContent = {
                        textRow(
                            rowOffset = 0.dp, displayedText = "Mask Generator\nSettings", textOffset = 15.dp,
                            fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 2
                        )
                    }
                )

                createCard(
                    xOffset = (bottomCardsX/3)*2, yOffset = bottomCardsY-100.dp,
                    width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                    themeColor = vm.themeColor, cardColor = 5,
                    cardContent = {
                        textRow(
                            rowOffset = 0.dp, displayedText = "Slice Generator\nSettings", textOffset = 15.dp,
                            fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = vm.themeColor, textColor = 2
                        )
                    }
                )

                // Bottom Bar Settings
                // If I don't have to look at it, it cant hurt me :D
                // Yes, it sucks
                // No, I DON'T know what im doing
                // No, it isn't more efficient
                // Yes, I need help
                bottomBar(vm, bottomCardsX, bottomCardsY)

                // Main Menu
                createMenu(
                    menuOffset = menuOffset, titleOffset = menuTitle, mainOffset = 60.dp, returnOffset = menuExit,
                    menuWidth = 300.dp, mainHeight = menuSize, elevation = 20.dp,
                    menuTitle = "Main Menu", returnTitle = "Return to main",
                    themeColor = vm.themeColor, cardColor = 5, buttonColor = 4, titleColor = 2, textColor = 2,
                    exitOperation = {
                        vm.menuPage = 0
                        vm.menuLines = 2
                    },
                    menuPages = {
                        // Main Menu
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 0), animationWidth = -2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.menuPage = 1
                                        vm.menuLines = 3
                                    },
                                    buttonText = "Export Settings", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.menuPage = 2
                                        vm.menuLines = 4
                                    },
                                    buttonText = "Select Theme", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Export Settings
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        val cb = Toolkit.getDefaultToolkit().systemClipboard
                                        val s = StringSelection(settingsToString())
                                        cb.setContents(s, s)
                                    },
                                    buttonText = "Export to Clipboard", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        settingsToCSV()
                                    },
                                    buttonText = "Export to CSV", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        outputLocation = SelectOutputPath()
                                    },
                                    buttonText = "Select Output", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Theme Selection
                        horizontalVisibilityPane(
                            visibility = (vm.menuPage == 2), animationWidth = 2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.themeColor = darkThemes
                                    },
                                    buttonText = "Theme: Dark", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.themeColor = lightThemes
                                    },
                                    buttonText = "Theme: Light", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.themeColor = celesteThemes
                                    },
                                    buttonText = "Theme: Celeste", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 150.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.themeColor = aqueousThemes
                                    },
                                    buttonText = "Theme: Aqueous", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )
                    }
                )

                // Settings Menu
                createMenu(
                    menuOffset = settingsOffset, titleOffset = settingsTitle, mainOffset = 60.dp, returnOffset = settingsExit,
                    menuWidth = 300.dp, mainHeight = settingsSize, elevation = 20.dp,
                    menuTitle = "Generation Settings", returnTitle = "Return to main",
                    themeColor = vm.themeColor, cardColor = 5, buttonColor = 4, titleColor = 2, textColor = 2,
                    exitOperation = {
                        vm.settingsPage = 0
                        vm.settingsLines = 3
                    },
                    menuPages = {
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 0), animationWidth = -2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.settingsPage = 1
                                        vm.configGenerator = true
                                        vm.settingsLines = 4
                                    },
                                    buttonText = "Select Generator", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.configSlices = true
                                    },
                                    buttonText = "Select Cut Type", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.settingsPage = 2
                                        vm.settingsLines = 1
                                    },
                                    buttonText = "Select Output", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Generator Type Selection
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 1), animationWidth = 2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.genType.set(GeneratorType.NONE)
                                        generatorType.set(GeneratorType.NONE)
                                        vm.genTypeA = "15"
                                        vm.genTypeB = "15"
                                        vm.selectedGenerator = 1
                                    },
                                    buttonText = "Square Generator", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {

                                    },
                                    buttonText = "Does Not Exist",  themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {

                                    },
                                    buttonText = "Does Not Exist", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 150.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {

                                    },
                                    buttonText = "Does Not Exist", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Output location, needs to be finished
                        horizontalVisibilityPane(
                            visibility = (vm.settingsPage == 2), animationWidth = 2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        // TODO: implement location picking cuz that doesnt exist yet
                                    },
                                    buttonText = "Select Output Location", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )
                    }
                )
            }
        }
    }
}