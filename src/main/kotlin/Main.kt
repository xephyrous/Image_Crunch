import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import utils.*
import java.io.FileInputStream

@Composable
@Preview
fun App() {
    // Settings
    var settingsLines by remember { mutableStateOf(2) }
    var genType by remember { mutableStateOf(generatorType) }
    var menuLines by remember { mutableStateOf(2) }
    var compactExportToggle by remember { mutableStateOf(compactExport) }
    var genBtnState by remember { mutableStateOf(false) }

    // Testing Stuff
    var caption by remember { mutableStateOf("theres NO PICTURE") }

    // holy mess
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var displayed by remember { mutableStateOf(false) }

    // Navigation Handler
    var settingsMain by remember { mutableStateOf(true) }
    var selectGenerator by remember { mutableStateOf(false) }
    var selectOutput by remember { mutableStateOf(false) }

    var mainMain by remember { mutableStateOf(true) }
    var exportSettings by remember { mutableStateOf(false) }
    var themeSettings by remember { mutableStateOf(false) }

    var themeColor by remember { mutableStateOf(darkThemes) }

    // Card Animations
    var menuCardState by remember { mutableStateOf(false) }
    var settingsCardState by remember { mutableStateOf(false) }
    val menuOffset by animateDpAsState(
        targetValue = (if (menuCardState) (if (!settingsCardState) 10 else 320) else (-310)).dp,
        label = "menuOffset"
    )
    val menuSize by animateDpAsState(
        targetValue = (menuLines*50).dp,
        label = "menuSize"
    )
    val settingsOffset by animateDpAsState(
        targetValue = (if (settingsCardState) 10 else (-310)).dp,
        label = "settingsOffset"
    )
    val settingsSize by animateDpAsState(
        targetValue = (settingsLines*50).dp,
        label = "settingsSize"
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar (
                title = {
                    Text("Image Crunch α",
                        color = themeColor[2]
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
                onClick = { scope.launch { snackbarHostState.showSnackbar("Clicked") } },
                text = { Text("Run", color = themeColor[2]) },
                icon = {
                    Icon( Icons.Sharp.PlayArrow, "Run",
                        tint = themeColor[3])
                       },
                backgroundColor = themeColor[4]
            )
        },
    ) {innerPadding ->
        Box(
            modifier = Modifier
                .background(themeColor[1])
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ){
                if(displayed) {
                    Image(
                        loadImageBitmap(inputStream = FileInputStream(selectedImage!!)),
                        "temp"
                    )
                }
                Text(caption, color = themeColor[2])
            }
            Card(
                modifier = Modifier
                    .width(300.dp)
                    .height(menuSize)
                    .offset(menuOffset,50.dp),
                backgroundColor = themeColor[5],
                elevation = 20.dp
            ) {
                // TODO: make this actual stuff in the menu
                // Main Menu
                AnimatedVisibility(
                    visible = mainMain,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 369)
                    ) {fullWidth -> -fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> -fullWidth*2 }
                ) {
                    Row() {
                        Button(
                            onClick = {
                                mainMain = false
                                exportSettings = true
                                menuLines = if(compactExportToggle) (2) else (3)
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
                                menuLines = 5
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
                    ) {fullWidth -> fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 }
                ) {
                    Row() {
                        Switch(
                            checked = compactExportToggle,
                            onCheckedChange = {
                                compactExportToggle = it
                                compactExport = compactExportToggle
                                if(!compactExport) {
                                    menuLines = 3
                                } else {
                                    menuLines = 2
                                }
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
                    Row(
                        modifier = Modifier
                            .offset(0.dp, (if(compactExportToggle) 50 else 100).dp)
                    ) {
                        Button(
                            onClick = {
                                exportSettings = false
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

                // Theme Selection
                AnimatedVisibility(
                    visible = themeSettings,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 }
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
                    Row(
                        modifier = Modifier
                            .offset(0.dp, 200.dp)
                    ) {
                        Button(
                            onClick = {
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
            }// End of the card

            Card(
                modifier = Modifier
                    .size(300.dp, settingsSize)
                    .offset(settingsOffset, 50.dp),
                elevation = 20.dp,
                backgroundColor = themeColor[5]
            ) {

                // settings main menu
                AnimatedVisibility(
                    visible = settingsMain,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 369)
                    ) {fullWidth -> -fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> -fullWidth*2 }
                ) {
                    Row() {
                        Button(
                            onClick = {
                                settingsMain = false
                                selectGenerator = true
                                settingsLines = 5
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
                                settingsLines = 2
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
                    ) {fullWidth -> fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 }
                ) {
                    Row() {
                        Button(
                            onClick = {
                                settingsMain = true
                                selectGenerator = false
                                settingsLines = 2
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
                                settingsMain = true
                                selectGenerator = false
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Heptagon Generator", color = themeColor[2])
                        }
                    }
                    Row(
                        modifier = Modifier.offset(0.dp, 100.dp)
                    ) {
                        Button(
                            onClick = {
                                settingsMain = true
                                selectGenerator = false
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Pentagon Generator", color = themeColor[2])
                        }
                    }
                    Row(
                        modifier = Modifier.offset(0.dp, 150.dp)
                    ) {
                        Button(
                            onClick = {
                                settingsMain = true
                                selectGenerator = false
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Octagon Generator", color = themeColor[2])
                        }
                    }
                    Row(
                        modifier = Modifier.offset(0.dp, 200.dp)
                    ) {
                        Button(
                            onClick = {
                                settingsMain = true
                                selectGenerator = false
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Return to Home", color = themeColor[2])
                        }
                    }
                }

                // Output location, needs to be finished
                AnimatedVisibility(
                    visible = selectOutput,
                    enter = slideInHorizontally(
                        animationSpec = tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 },
                    exit = slideOutHorizontally(
                        tween(durationMillis = 369)
                    ) {fullWidth -> fullWidth*2 }
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
                    Row(
                        modifier = Modifier.offset(0.dp, 50.dp)
                    ) {
                        Button(
                            onClick = {
                                settingsMain = true
                                selectOutput = false
                                settingsLines = 2
                            },
                            modifier = Modifier.offset(25.dp, 0.dp).width(250.dp),
                            colors = ButtonDefaults.buttonColors(backgroundColor = themeColor[4])
                        ) {
                            Text("Return to Home", color = themeColor[2])
                        }
                    }
                }
            }// End of the card 2.0
        }
    }
}

fun main() = application {
    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(150.dp, 150.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}