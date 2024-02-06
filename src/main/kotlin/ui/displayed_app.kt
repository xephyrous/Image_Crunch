package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.List
import androidx.compose.material.icons.sharp.PlayArrow
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.app.ImageFileSelection
import utils.app.settingsToCSV
import utils.app.settingsToString
import utils.images.*
import utils.storage.*

@Suppress("DuplicatedCode")
@Composable
fun App() {
    val vm = remember { ViewModel() }

    // Configuration Settings
    val numbersOnly = Regex("^\\d+\$")

    val density = LocalDensity.current

    //Card Animations
    val menuOffset by animateDpAsState(targetValue = (if (vm.menuCardState) (if (!vm.settingsCardState) 10 else 320) else (-310)).dp)
    val menuSize by animateDpAsState(targetValue = (vm.menuLines * 50).dp)
    val menuTitle by animateDpAsState(targetValue = if (vm.menuCardState) 5.dp else 60.dp)
    val menuExit by animateDpAsState(targetValue = if (vm.mainMain) (60 + ((vm.menuLines - 1) * 50)).dp else (65 + (vm.menuLines * 50)).dp)
    val settingsOffset by animateDpAsState(targetValue = (if (vm.settingsCardState) 10 else (-310)).dp)
    val settingsSize by animateDpAsState(targetValue = (vm.settingsLines * 50).dp)
    val settingsTitle by animateDpAsState(targetValue = if (vm.settingsCardState) 5.dp else 60.dp)
    val settingsExit by animateDpAsState(targetValue = if (vm.settingsMain) (60 + ((vm.settingsLines - 1) * 50)).dp else (65 + (vm.settingsLines * 50)).dp)
    val bottomCardsY by animateDpAsState(targetValue = vm.screenHeight)
    val bottomCardsX by animateDpAsState(targetValue = vm.screenWidth)

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
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Build,
                                contentDescription = "Settings",
                                tint = vm.themeColor[3]
                            )
                        }
                        IconButton(onClick = {
                            vm.menuCardState = !vm.menuCardState
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
//                    .background(vm.themeColor[1])
                    .background(
//                        Brush.sweepGradient(listOf(vm.themeColor[1], vm.themeColor[5], vm.themeColor[1], vm.themeColor[5], vm.themeColor[1]))
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
                    }
            ) {
                if (vm.displayed) {
                    AsyncImage(
                        load = { loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedImage!!)) },
                        contentDescription = "The Passed Image",
                        painterFor = { remember { BitmapPainter(it) } }
                    )
                    if (vm.nodeDisplay) {
                        AsyncImage(
                            load = { loadImageBitmap(inputStream = bufferedImageToOutputStream(vm.displayedNodes!!)) },
                            contentDescription = "The Image Mask",
                            painterFor = { remember { BitmapPainter(it) } }
                        )
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
                verticalVisibilityPane(
                    visibility = vm.configGenerator, animationHeight = 2, duration = 369, paneContent = {
                        createCard(
                            xOffset = 0.dp, yOffset = bottomCardsY-220.dp,
                            width = bottomCardsX/3, height = 500.dp, elevation = 5.dp,
                            themeColor = vm.themeColor, cardColor = 5,
                            cardContent = {
                                AnimatedVisibility(
                                    visible = vm.squareGenerator,
                                    enter = slideInVertically(
                                        animationSpec = tween(durationMillis = 369)
                                    ) { fullHeight -> fullHeight * 2 },
                                    exit = slideOutVertically(
                                        tween(durationMillis = 369)
                                    ) { fullHeight -> fullHeight * 2 }
                                ) {
                                    Row() {
                                        Text(
                                            "Number of Rows:", color = vm.themeColor[2],
                                            modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                            fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.offset(y = 50.dp)
                                    ) {
                                        TextField(
                                            value = vm.genTypeA,
                                            onValueChange = {
                                                if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                                    vm.genTypeA = it
                                                    if(vm.genTypeA.isNotEmpty()) {
                                                        squareRows.set(vm.genTypeA.toInt())
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size((vm.screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp)
                                                .onKeyEvent {
                                                    if (it.key == Key.Enter) {
                                                        if(squareRows.value() > 0 && vm.displayedImage != null) {
                                                            vm.nodeDisplay = false
                                                            vm.displayedNodes = createNodeMask(
                                                                generateNodes(GeneratorType.SQUARE)
                                                            )
                                                            vm.nodeDisplay = true
                                                        }
                                                    }
                                                    true
                                                }
                                                .onFocusChanged {
                                                    if (!it.isFocused) {
                                                        if(squareRows.value() > 0 && vm.displayedImage != null) {
                                                            vm.nodeDisplay = false
                                                            vm.displayedNodes = createNodeMask(
                                                                generateNodes(GeneratorType.SQUARE)
                                                            )
                                                            vm.nodeDisplay = true
                                                        }
                                                    }
                                                },
                                            readOnly = false,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.offset(y = 110.dp)
                                    ) {
                                        Text(
                                            "Number of Columns:", color = vm.themeColor[2],
                                            modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                            fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.offset(y = 160.dp)
                                    ) {
                                        TextField(
                                            value = vm.genTypeB,
                                            onValueChange = {
                                                if(it.length < 5 && (it.matches(numbersOnly) || it.isEmpty())) {
                                                    vm.genTypeB = it
                                                    if(vm.genTypeB.isNotEmpty()) squareColumns.set(vm.genTypeB.toInt())
                                                }
                                            },
                                            modifier = Modifier.size((vm.screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp)
                                                .onKeyEvent {
                                                    if (it.key == Key.Enter) {
                                                        if(squareColumns.value() > 0 && vm.displayedImage != null) {
                                                            vm.nodeDisplay = false
                                                            vm.displayedNodes = createNodeMask(
                                                                generateNodes(GeneratorType.SQUARE)
                                                            )
                                                            vm.nodeDisplay = true
                                                        }
                                                    }
                                                    true
                                                }
                                                .onFocusChanged {
                                                    if (!it.isFocused) {
                                                        if(squareColumns.value() > 0 && vm.displayedImage != null) {
                                                            vm.nodeDisplay = false
                                                            vm.displayedNodes = createNodeMask(
                                                                generateNodes(GeneratorType.SQUARE)
                                                            )
                                                            vm.nodeDisplay = true
                                                        }
                                                    }
                                                },
                                            readOnly = false,
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        )
                                    }
                                }
                            }
                        )
                    }
                )
                verticalVisibilityPane(
                    visibility = vm.configMasks, animationHeight = 2, duration = 369, paneContent = {
                        createCard(
                            xOffset = bottomCardsX/3, yOffset = bottomCardsY-150.dp,
                            width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                            themeColor = vm.themeColor, cardColor = 5,
                            cardContent = {}
                        )
                    }
                )
                verticalVisibilityPane(
                    visibility = vm.configSlices, animationHeight = 2, duration = 369, paneContent = {
                        createCard(
                            xOffset = (bottomCardsX/3)*2, yOffset = bottomCardsY-150.dp,
                            width = bottomCardsX/3, height = 400.dp, elevation = 5.dp,
                            themeColor = vm.themeColor, cardColor = 5,
                            cardContent = {
                                textRow(
                                    rowOffset = 0.dp, displayedText = "Filla Text", textOffset = 5.dp,
                                    fontSize = 40.sp, font = FontWeight.Normal, themeColor = vm.themeColor, textColor = 2
                                )
                            }
                        )
                    }
                )

                // Main Menu
                createMenu(
                    menuOffset = menuOffset, titleOffset = menuTitle, mainOffset = 60.dp, returnOffset = menuExit,
                    menuWidth = 300.dp, mainHeight = menuSize, elevation = 20.dp,
                    menuTitle = "Main Menu", returnTitle = "Return to main",
                    themeColor = vm.themeColor, cardColor = 5, buttonColor = 4, titleColor = 2, textColor = 2,
                    exitOperation = {
                        vm.exportSettings = false
                        vm.themeSettings = false
                        vm.mainMain = true
                        vm.menuLines = 2
                    },
                    menuPages = {
                        // Main Menu
                        horizontalVisibilityPane(
                            visibility = vm.mainMain, animationWidth = -2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.mainMain = false
                                        vm.exportSettings = true
                                        vm.menuLines = if (vm.compactExportToggle) (1) else (2)
                                    },
                                    buttonText = "Export Settings", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.mainMain = false
                                        vm.themeSettings = true
                                        vm.menuLines = 4
                                    },
                                    buttonText = "Select Theme", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Export Settings
                        horizontalVisibilityPane(
                            visibility = vm.exportSettings, animationWidth = 2, duration = 369, paneContent = {
                                Row() {
                                    Switch(
                                        checked = vm.compactExportToggle,
                                        onCheckedChange = {
                                            vm.compactExportToggle = it
                                            compactExport = vm.compactExportToggle
                                            vm.menuLines = if (!compactExport) { 2 } else { 1 }
                                        },
                                        modifier = Modifier.offset(50.dp, 0.dp).width(25.dp)
                                    )
                                    Button(
                                        onClick = {
                                            if (compactExport) {
                                                settingsToString()
                                            } else {
                                                settingsToCSV()
                                            }
                                        },
                                        modifier = Modifier.offset(75.dp, 0.dp).width(150.dp),
                                        colors = ButtonDefaults.buttonColors(backgroundColor = vm.themeColor[4])
                                    ) {
                                        Text("Button?", color = vm.themeColor[2])
                                    }
                                }
                                if (!vm.compactExportToggle) {
                                    Row(
                                        modifier = Modifier
                                            .offset(0.dp, 50.dp)
                                    ) {
                                        Text("Select Output Here", color = vm.themeColor[2])
                                    }
                                }
                            }
                        )

                        // Theme Selection
                        horizontalVisibilityPane(
                            visibility = vm.themeSettings, animationWidth = 2, duration = 369, paneContent = {
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
                        vm.selectGenerator = false
                        vm.selectOutput = false
                        vm.settingsMain = true
                        vm.settingsLines = 3
                    },
                    menuPages = {
                        horizontalVisibilityPane(
                            visibility = vm.settingsMain, animationWidth = -2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.settingsMain = false
                                        vm.configGenerator = true
                                        vm.selectGenerator = true
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
                                        vm.settingsMain = false
                                        vm.selectOutput = true
                                        vm.settingsLines = 1
                                    },
                                    buttonText = "Select Output", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                            }
                        )

                        // Generator Type Selection
                        horizontalVisibilityPane(
                            visibility = vm.selectGenerator, animationWidth = 2, duration = 369, paneContent = {
                                buttonRow(
                                    rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.genType.set(GeneratorType.NONE)
                                        generatorType.set(GeneratorType.NONE)
                                        vm.genTypeA = "15"
                                        vm.genTypeB = "15"
                                        vm.squareGenerator = true
                                    },
                                    buttonText = "Square Generator", themeColor = vm.themeColor,
                                    buttonColor = 4, textColor = 2
                                )
                                buttonRow(
                                    rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                    buttonEvent = {
                                        vm.displayed = false
                                    },
                                    buttonText = "nuke the bitch",  themeColor = vm.themeColor,
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
                            visibility = vm.selectOutput, animationWidth = 2, duration = 369, paneContent = {
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