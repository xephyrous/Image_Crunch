import androidx.compose.animation.*
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import utils.*
import java.awt.Dimension
import java.io.FileInputStream

@Suppress("DuplicatedCode")
@Composable
@Preview
fun App() {
    // Settings
    var settingsLines by remember { mutableStateOf(2) }
    var genType by remember { mutableStateOf(generatorType) }
    var menuLines by remember { mutableStateOf(2) }
    var compactExportToggle by remember { mutableStateOf(compactExport) }

    // Testing Stuff
    var caption by remember { mutableStateOf("theres NO PICTURE") }

    // The good shi
    var displayed by remember { mutableStateOf(false) }

    var settingsMain by remember { mutableStateOf(true) }
    var selectGenerator by remember { mutableStateOf(false) }
    var selectOutput by remember { mutableStateOf(false) }

    var configGenerator by remember { mutableStateOf(false) }
    var configMasks by remember { mutableStateOf(false) }
    var configSlices by remember { mutableStateOf(false) }

    var squareGenerator by remember { mutableStateOf(true) }

    var mainMain by remember { mutableStateOf(true) }
    var exportSettings by remember { mutableStateOf(false) }
    var themeSettings by remember { mutableStateOf(false) }

    var themeColor by remember { mutableStateOf(darkThemes) }

    var screenWidth by remember { mutableStateOf(1200.dp) }
    var screenHeight by remember { mutableStateOf(800.dp) }
    val density = LocalDensity.current

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
                                  else -> {
                                      println("how...")
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
                    Row() {
                        Text(
                            "Node Generator\nSettings", color = themeColor[2],
                            modifier = Modifier.fillMaxSize().offset(y = 15.dp),
                            fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Card(
                    modifier = Modifier.offset(bottomCardsX/3, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                    backgroundColor = themeColor[5],
                    elevation = 5.dp
                ) {
                    Row() {
                        Text(
                            "Mask Generator\nSettings", color = themeColor[2],
                            modifier = Modifier.fillMaxSize().offset(y = 15.dp),
                            fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Card(
                    modifier = Modifier.offset((bottomCardsX/3)*2, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                    backgroundColor = themeColor[5],
                    elevation = 5.dp
                ) {
                    Row() {
                        Text(
                            "Slice Generator\nSettings", color = themeColor[2],
                            modifier = Modifier.fillMaxSize().offset(y = 15.dp),
                            fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Bottom Bar Settings
                AnimatedVisibility(
                    visible = configGenerator,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 },
                    exit = slideOutVertically(
                        tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 }
                ) {
                    Card(
                        modifier = Modifier.offset(0.dp, bottomCardsY-150.dp).size(bottomCardsX/3, 400.dp),
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
                                Button(
                                    onClick = {
                                        // :D
                                    },
                                    modifier = Modifier.offset(10.dp, 0.dp).width((bottomCardsX/3)-20.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                                ) {
                                    Text("button?", color = themeColor[2])
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = configMasks,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 },
                    exit = slideOutVertically(
                        tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 }
                ) {
                    Card(
                        modifier = Modifier.offset(bottomCardsX/3, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                        backgroundColor = themeColor[5],
                        elevation = 5.dp
                    ) {

                    }
                }
                AnimatedVisibility(
                    visible = configSlices,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 },
                    exit = slideOutVertically(
                        tween(durationMillis = 369)
                    ) { fullHeight -> fullHeight * 2 }
                ) {
                    Card(
                        modifier = Modifier.offset((bottomCardsX/3)*2, bottomCardsY-100.dp).size(bottomCardsX/3, 400.dp),
                        backgroundColor = themeColor[5],
                        elevation = 5.dp
                    ) {

                    }
                }

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
                    Row {
                        Text(
                            "Main Menu", color = themeColor[2],
                            modifier = Modifier.fillMaxSize().offset(y = 10.dp),
                            fontSize = 30.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(menuOffset, menuExit),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    Row() {
                        Button(
                            onClick = {
                                exportSettings = false
                                themeSettings = false
                                mainMain = true
                                menuLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Return to main", color = themeColor[2])
                        }
                    }
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
                    AnimatedVisibility(
                        visible = mainMain,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> -fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> -fullWidth * 2 }
                    ) {
                        Row() {
                            Button(
                                onClick = {
                                    mainMain = false
                                    exportSettings = true
                                    menuLines = if (compactExportToggle) (1) else (2)
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Export Settings", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier.offset(0.dp, 50.dp)
                        ) {
                            Button(
                                onClick = {
                                    mainMain = false
                                    themeSettings = true
                                    menuLines = 4
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Select Theme", color = themeColor[2])
                            }
                        }
                    }

                    // Export Settings
                    AnimatedVisibility(
                        visible = exportSettings,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 }
                    ) {
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

                    // Theme Selection
                    AnimatedVisibility(
                        visible = themeSettings,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 }
                    ) {
                        Row(
                        ) {
                            Button(
                                onClick = {
                                    themeColor = darkThemes
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Theme: Dark", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier
                                .offset(0.dp, 50.dp)
                        ) {
                            Button(
                                onClick = {
                                    themeColor = lightThemes
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Theme: Light", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier
                                .offset(0.dp, 100.dp)
                        ) {
                            Button(
                                onClick = {
                                    themeColor = celesteThemes
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Theme: Celeste", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier
                                .offset(0.dp, 150.dp)
                        ) {
                            Button(
                                onClick = {
                                    themeColor = aqueousThemes
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Theme: Aqueous", color = themeColor[2])
                            }
                        }
                    }
                }// End of the card

                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(settingsOffset, settingsTitle),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    Row {
                        Text(
                            "Generation Settings", color = themeColor[2],
                            modifier = Modifier.fillMaxSize().offset(y = 10.dp),
                            fontSize = 30.sp, textAlign = TextAlign.Center
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp)
                        .offset(settingsOffset, settingsExit),
                    backgroundColor = themeColor[5],
                    elevation = 20.dp
                ) {
                    Row() {
                        Button(
                            onClick = {
                                selectGenerator = false
                                configGenerator = false
                                selectOutput = false
                                settingsMain = true
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Return to main", color = themeColor[2])
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .size(300.dp, settingsSize)
                        .offset(settingsOffset, 60.dp),
                    elevation = 20.dp,
                    backgroundColor = themeColor[5]
                ) {

                    // settings main menu
                    AnimatedVisibility(
                        visible = settingsMain,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> -fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> -fullWidth * 2 }
                    ) {
                        Row() {
                            Button(
                                onClick = {
                                    settingsMain = false
                                    selectGenerator = true
                                    settingsLines = 4
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Select Generator", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier.offset(0.dp, 50.dp)
                        ) {
                            Button(
                                onClick = {
                                    settingsMain = false
                                    selectOutput = true
                                    settingsLines = 1
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Select Output", color = themeColor[2])
                            }
                        }
                    }

                    // Generator Type Selection
                    AnimatedVisibility(
                        visible = selectGenerator,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 }
                    ) {
                        Row() {
                            Button(
                                onClick = {
                                    genType = 0
                                    generatorType = 0
                                    configGenerator = true
                                    squareGenerator = true
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Square Generator", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier.offset(0.dp, 50.dp)
                        ) {
                            Button(
                                onClick = {

                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Does Not Exist", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier.offset(0.dp, 100.dp)
                        ) {
                            Button(
                                onClick = {

                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Does Not Exist", color = themeColor[2])
                            }
                        }
                        Row(
                            modifier = Modifier.offset(0.dp, 150.dp)
                        ) {
                            Button(
                                onClick = {

                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Does Not Exist", color = themeColor[2])
                            }
                        }
                    }
                    // Output location, needs to be finished
                    AnimatedVisibility(
                        visible = selectOutput,
                        enter = slideInHorizontally(
                            animationSpec = tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 },
                        exit = slideOutHorizontally(
                            tween(durationMillis = 369)
                        ) { fullWidth -> fullWidth * 2 }
                    ) {
                        Row() {
                            Button(
                                onClick = {
                                    // TODO: implement location picking cuz that doesnt exist yet
                                },
                                modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                            ) {
                                Text("Select Output Location", color = themeColor[2])
                            }
                        }
                    }
                } // End of the card 2.0
            }
        }
    }
}

fun main() = application {
    loadedImageSize = Dimension(42, 42)
    val nodes = squareNodeGenerator(6, 5)
    val cutMask = squareCutGenerator(nodes, 6, 5)

    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(150.dp, 150.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}