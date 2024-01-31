package ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import utils.*
import java.io.FileInputStream


@Suppress("DuplicatedCode")
@Composable
fun App() {
    // Settings
    var settingsLines by remember { mutableStateOf(3) }
    var genType by remember { mutableStateOf(generatorType) }
    var menuLines by remember { mutableStateOf(2) }
    var compactExportToggle by remember { mutableStateOf(compactExport) }

    // Testing Stuff
    var caption by remember { mutableStateOf(squareRows.toString()) }

    // The good shi
    var displayed by remember { mutableStateOf(false) }

    var settingsMain by remember { mutableStateOf(true) }
    var selectGenerator by remember { mutableStateOf(false) }
    var selectOutput by remember { mutableStateOf(false) }

    var configGenerator by remember { mutableStateOf(false) }
    var configMasks by remember { mutableStateOf(false) }
    var configSlices by remember { mutableStateOf(false) }

    var squareGenerator by remember { mutableStateOf(false) }

    var mainMain by remember { mutableStateOf(true) }
    var exportSettings by remember { mutableStateOf(false) }
    var themeSettings by remember { mutableStateOf(false) }

    var themeColor by remember { mutableStateOf(darkThemes) }

    var screenWidth by remember { mutableStateOf(1200.dp) }
    var screenHeight by remember { mutableStateOf(800.dp) }
    val density = LocalDensity.current

    // Configuration Settings
    val numbersOnly = remember { Regex("^\\d+\$") }

    var genTypeA by remember { mutableStateOf("") }
    var genTypeB by remember { mutableStateOf("") }

    // Card Animations
    var menuCardState by remember { mutableStateOf(false) }
    var settingsCardState by remember { mutableStateOf(false) }
    val menuOffset by animateDpAsState(
        targetValue = (if (menuCardState) (if (!settingsCardState) 10 else 320) else (-310)).dp,
        label = "menuOffset"
    )
    val menuSize by animateDpAsState(
        targetValue = (menuLines * 50).dp,
        label = "menuSize"
    )
    val menuTitle by animateDpAsState(
        targetValue = if (menuCardState) 5.dp else 60.dp,
        label = "menuTitle"
    )
    val menuExit by animateDpAsState(
        targetValue = if (mainMain) (60 + ((menuLines - 1) * 50)).dp else (65 + (menuLines * 50)).dp,
        label = "exitMain"
    )

    val settingsOffset by animateDpAsState(
        targetValue = (if (settingsCardState) 10 else (-310)).dp,
        label = "settingsOffset"
    )
    val settingsSize by animateDpAsState(
        targetValue = (settingsLines * 50).dp,
        label = "settingsSize"
    )
    val settingsTitle by animateDpAsState(
        targetValue = if (settingsCardState) 5.dp else 60.dp,
        label = "settingsTitle"
    )
    val settingsExit by animateDpAsState(
        targetValue = if (settingsMain) (60 + ((settingsLines - 1) * 50)).dp else (65 + (settingsLines * 50)).dp,
        label = "exitSettings"
    )

    val bottomCardsY by animateDpAsState(
        targetValue = screenHeight,
        label = "bottomCardsY"
    )
    val bottomCardsX by animateDpAsState(
        targetValue = screenWidth,
        label = "bottomCardsX"
    )

    AppTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Image Chomp",
                            color = themeColor[2],
                            fontSize = 30.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    },
                    backgroundColor = themeColor[0],
                    actions = {
                        // holy spaghetti
                        IconButton(onClick = {
                            if (!displayed) {
                                selectedImage = ImageFileSelection()
                            } else {
                                val tempImage = ImageFileSelection()
                                if (tempImage != null) {
                                    selectedImage = tempImage
                                }
                            }

                            if (selectedImage == null) {
                                caption = "theres NO PICTURE"
                                displayed = false
                            } else {
                                caption = "omg it worked"
                                displayed = true
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Add,
                                contentDescription = "Select Image",
                                tint = themeColor[3]
                            )
                        }

                        IconButton(onClick = {
                            settingsCardState = !settingsCardState
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.Build,
                                contentDescription = "Settings",
                                tint = themeColor[3]
                            )
                        }
                        IconButton(onClick = {
                            menuCardState = !menuCardState
                        }) {
                            Icon(
                                imageVector = Icons.Sharp.List,
                                contentDescription = "Test Text",
                                tint = themeColor[3]
                            )
                        }
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = {
                        when (generatorType) {
                            0 -> {
                                squareNodeGenerator(squareRows, squareColumns)
                            }
                        }
                    },
                    text = { Text("Run", color = themeColor[2]) },
                    icon = {
                        Icon(
                            Icons.Sharp.PlayArrow, "Run",
                            tint = themeColor[3]
                        )
                    },
                    backgroundColor = themeColor[4],
                    modifier = Modifier.offset(0.dp, (-100).dp)
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .background(themeColor[1])
                    .fillMaxSize()
                    .onGloballyPositioned {
                        screenWidth = with(density) {it.size.width.toDp()}
                        screenHeight = with(density) {it.size.height.toDp()}
                    }
            ) {
                // Bottom Bar
                Card(
                    modifier = Modifier.offset(0.dp, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                    backgroundColor = themeColor[5],
                    elevation = 5.dp
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Node Generator\nSettings", textOffset = 15.dp,
                        fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = themeColor, textColor = 2
                    )
                }
                Card(
                    modifier = Modifier.offset(bottomCardsX/3, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                    backgroundColor = themeColor[5],
                    elevation = 5.dp
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Mask Generator\nSettings", textOffset = 15.dp,
                        fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = themeColor, textColor = 2
                    )
                }
                Card(
                    modifier = Modifier.offset((bottomCardsX/3)*2, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                    backgroundColor = themeColor[5],
                    elevation = 5.dp
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Slice Generator\nSettings", textOffset = 15.dp,
                        fontSize = 30.sp, font = FontWeight.SemiBold, themeColor = themeColor, textColor = 2
                    )
                }

                // Bottom Bar Settings
                verticalVisibilityPane(
                    visibility = configGenerator, animationHeight = 2, duration = 369, paneContent = {
                        Card(
                            modifier = Modifier.offset(0.dp, bottomCardsY-220.dp).size(bottomCardsX/3, 500.dp),
                            backgroundColor = themeColor[5],
                            elevation = 5.dp
                        ) {
                            AnimatedVisibility(
                                visible = squareGenerator,
                                enter = slideInVertically(
                                    animationSpec = tween(durationMillis = 369)
                                ) { fullHeight -> fullHeight * 2 },
                                exit = slideOutVertically(
                                    tween(durationMillis = 369)
                                ) { fullHeight -> fullHeight * 2 }
                            ) {
                                Row() {
                                    Text(
                                        "Number of Rows:", color = themeColor[2],
                                        modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                        fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                    )
                                }
                                Row(
                                    modifier = Modifier.offset(y = 50.dp)
                                ) {
                                    TextField(
                                        value = genTypeA,
                                        onValueChange = {
                                            if(it.length < 10 && (it.matches(numbersOnly) || it.isEmpty())) {
                                                genTypeA = it
                                                if(genTypeA.isNotEmpty()) squareRows = genTypeA.toInt()
                                            }
                                        },
                                        modifier = Modifier.size((screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp),
                                        readOnly = false,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    )
                                }
                                Row(
                                    modifier = Modifier.offset(y = 110.dp)
                                ) {
                                    Text(
                                        "Number of Columns:", color = themeColor[2],
                                        modifier = Modifier.fillMaxSize().offset(y= 5.dp),
                                        fontSize = 40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Normal
                                    )
                                }
                                Row(
                                    modifier = Modifier.offset(y = 160.dp)
                                ) {
                                    TextField(
                                        value = genTypeB,
                                        onValueChange = {
                                            if(it.length < 10 && (it.matches(numbersOnly) || it.isEmpty())) {
                                                genTypeB = it
                                                if(genTypeB.isNotEmpty()) squareColumns = genTypeA.toInt()
                                            }
                                        },
                                        modifier = Modifier.size((screenWidth/3)-10.dp, 50.dp).offset(5.dp, 5.dp),
                                        readOnly = false,
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    )
                                }
                            }
                        }
                    }
                )
                verticalVisibilityPane(
                    visibility = configMasks, animationHeight = 2, duration = 369, paneContent = {
                        Card(
                            modifier = Modifier.offset(bottomCardsX/3, bottomCardsY-150.dp).size(bottomCardsX/3, 400.dp),
                            backgroundColor = themeColor[5],
                            elevation = 5.dp
                        ) {

                        }
                    }
                )

                verticalVisibilityPane(
                    visibility = configSlices, animationHeight = 2, duration = 369, paneContent = {
                        Card(
                            modifier = Modifier.offset((bottomCardsX/3)*2, bottomCardsY-150.dp).size(bottomCardsX/3, 400.dp),
                            backgroundColor = themeColor[5],
                            elevation = 5.dp
                        ) {
                            textRow(
                                rowOffset = 0.dp, displayedText = "Filla Text", textOffset = 5.dp,
                                fontSize = 40.sp, font = FontWeight.Normal, themeColor = themeColor, textColor = 2
                            )
                        }
                    }
                )

                Column(
                    modifier = Modifier.padding(innerPadding).fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    if (displayed) {
                        Image(
                            loadImageBitmap(inputStream = FileInputStream(selectedImage!!)),
                            "temp"
                        )
                    }
                    Text(caption, color = themeColor[2])
                }

                // Title Card
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(menuOffset, menuTitle),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Main Menu", textOffset = 10.dp,
                        fontSize = 30.sp, font = FontWeight.Normal,
                        themeColor = themeColor, textColor = 2
                    )
                }
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(menuOffset, menuExit),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    buttonRow(
                        rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                        buttonEvent = {
                            exportSettings = false
                            themeSettings = false
                            mainMain = true
                            menuLines = 2
                        },
                        buttonText = "Return to main", themeColor = themeColor
                    )
                }
                // Main Menu Card
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(menuSize)
                        .offset(menuOffset, 60.dp),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    // Main Menu
                    horizontalVisibilityPane(
                        visibility = mainMain, animationWidth = -2, duration = 369, paneContent = {
                            buttonRow(
                                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    mainMain = false
                                    exportSettings = true
                                    menuLines = if (compactExportToggle) (1) else (2)
                                },
                                buttonText = "Export Settings", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    mainMain = false
                                    themeSettings = true
                                    menuLines = 4
                                },
                                buttonText = "Select Theme", themeColor = themeColor
                            )
                        }
                    )

                    // Export Settings
                    horizontalVisibilityPane(
                        visibility = exportSettings, animationWidth = 2, duration = 369, paneContent = {
                            Row() {
                                Switch(
                                    checked = compactExportToggle,
                                    onCheckedChange = {
                                        compactExportToggle = it
                                        compactExport = compactExportToggle
                                        menuLines = if (!compactExport) { 2 } else { 1 }
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
                                    colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                                ) {
                                    Text("Button?", color = themeColor[2])
                                }
                            }
                            if (!compactExportToggle) {
                                Row(
                                    modifier = Modifier
                                        .offset(0.dp, 50.dp)
                                ) {
                                    Text("Select Output Here", color = themeColor[2])
                                }
                            }
                        }
                    )

                    // Theme Selection
                    horizontalVisibilityPane(
                        visibility = themeSettings, animationWidth = 2, duration = 369, paneContent = {
                            buttonRow(
                                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    themeColor = darkThemes
                                },
                                buttonText = "Theme: Dark", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    themeColor = lightThemes
                                },
                                buttonText = "Theme: Light", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    themeColor = celesteThemes
                                },
                                buttonText = "Theme: Celeste", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 150.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    themeColor = aqueousThemes
                                },
                                buttonText = "Theme: Aqueous", themeColor = themeColor
                            )
                        }
                    )
                }// End of the card

                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(settingsOffset, settingsTitle),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    textRow(
                        rowOffset = 0.dp, displayedText = "Generation Settings", textOffset = 10.dp,
                        fontSize = 30.sp, font = FontWeight.Normal,
                        themeColor = themeColor, textColor = 2
                    )
                }
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(settingsOffset, settingsExit),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    buttonRow(
                        rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                        buttonEvent = {
                            selectGenerator = false
                            selectOutput = false
                            settingsMain = true
                            settingsLines = 3
                        },
                        buttonText = "Return to main", themeColor = themeColor
                    )
                }
                Card(
                    modifier = Modifier
                        .size(300.dp, settingsSize)
                        .offset(settingsOffset, 60.dp),
                    elevation = 20.dp,
                    backgroundColor = themeColor[5]
                ) {

                    // settings main menu
                    horizontalVisibilityPane(
                        visibility = settingsMain, animationWidth = -2, duration = 369, paneContent = {
                            buttonRow(
                                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    settingsMain = false
                                    configGenerator = true
                                    selectGenerator = true
                                    settingsLines = 4
                                },
                                buttonText = "Select Generator", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    configSlices = true
                                },
                                buttonText = "Select Cut Type", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    settingsMain = false
                                    selectOutput = true
                                    settingsLines = 1
                                },
                                buttonText = "Select Output", themeColor = themeColor
                            )
                        }
                    )

                    // Generator Type Selection
                    horizontalVisibilityPane(
                        visibility = selectGenerator, animationWidth = 2, duration = 369, paneContent = {
                            buttonRow(
                                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    genType = 0
                                    generatorType = 0
                                    genTypeA = "15"
                                    genTypeB = "15"
                                    squareGenerator = true
                                },
                                buttonText = "Square Generator", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 50.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {

                                },
                                buttonText = "Does Not Exist", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 100.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {

                                },
                                buttonText = "Does Not Exist", themeColor = themeColor
                            )
                            buttonRow(
                                rowOffset = 150.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {

                                },
                                buttonText = "Does Not Exist", themeColor = themeColor
                            )
                        }
                    )

                    // Output location, needs to be finished
                    horizontalVisibilityPane(
                        visibility = selectOutput, animationWidth = 2, duration = 369, paneContent = {
                            buttonRow(
                                rowOffset = 0.dp, buttonOffset = 25.dp, width = 250.dp,
                                buttonEvent = {
                                    // TODO: implement location picking cuz that doesnt exist yet
                                },
                                buttonText = "Select Output Location", themeColor = themeColor
                            )
                        }
                    )
                } // End of the card 2.0
            }
        }
    }
}