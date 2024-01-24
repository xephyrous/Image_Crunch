import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
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
                    Text("Image Crunch α")
                },

                actions = { //TODO: Add image to main panel
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
                            contentDescription = "Select Image"
                        )
                    }
                    
                    IconButton(onClick = {
                        settingsCardState = !settingsCardState
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.Build,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = {
                        menuCardState = !menuCardState
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.List,
                            contentDescription = "Test Text"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { scope.launch { snackbarHostState.showSnackbar("Clicked") } },
                text = { Text("Run") },
                icon = { Icon( Icons.Sharp.PlayArrow, "Run") }
            )
        },
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            if(displayed) {
                Image(
                    loadImageBitmap(inputStream = FileInputStream(selectedImage!!)),
                    "temp"
                )
            }
            Text(caption)
        }
        Card(
            modifier = Modifier
                // why does enabling .animateContentSize() BREAK EVERYTHING
                .width(300.dp)
                .height(menuSize)
                .offset(menuOffset,50.dp),
            elevation = 20.dp
        ) {
            // TODO: make this actual stuff in the menu
            // Basic Menu
            Row() {
                // TODO: make this switch animate the other menu stuff moving down and the option fading in (suffer)
                Switch(
                    checked = compactExportToggle,
                    onCheckedChange = {
                        compactExportToggle = it
                        compactExport = compactExportToggle
                        if(!compactExport) {
                            menuLines++
                        } else {
                            menuLines--
                        }
                    }
                )
                Text(
                    text = compactExport.toString(),
                    modifier = Modifier
                        .offset(5.dp, 10.dp)
                )
                Button(
                    onClick = {
                        if (compactExport) {
                            settingsToString()
                        } else {
                            settingsToCSV()
                        }
                    },
                    modifier = Modifier.offset(50.dp, 0.dp)
                    ) {
                    Text("Export")
                }
            }
            if (!compactExportToggle) {
                Row(
                    modifier = Modifier
                        .offset(0.dp, 50.dp)
                ) {
                    Text("Balls")
                }
            }
            Row(
                modifier = Modifier
                    .offset(0.dp, (if(compactExportToggle) 50 else 100).dp)
            ) {
                Text("Import")
            }
        }// End of the card

        Card(
            modifier = Modifier
                // how do i scale the size of this?!?!!??!
                .size(300.dp, settingsSize)
                .offset(settingsOffset, 50.dp),
            elevation = 20.dp
        ) {
            // TODO: WHAT THE FLUFF IS A SETTINGS
            // This is a navigatable settings menu (kinda)
            // TODO: add this functionality to menu menu
            // This handles animations of objects and its GREAT
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
                        modifier = Modifier
                            .offset(50.dp, 0.dp)
                    ) {
                        Text("Select Generator")
                    }
                }
                Row() {
                    Button(
                        onClick = {
                            settingsMain = false
                            selectGenerator = true
                            settingsLines = 5
                        },
                        modifier = Modifier
                            .offset(50.dp, 50.dp)
                    ) {
                        Text("Filler Button")
                    }
                }
            }
            AnimatedVisibility(
                visible = selectGenerator,
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
                            settingsMain = true
                            selectGenerator = false
                            settingsLines = 2
                        },
                        modifier = Modifier
                            .offset(50.dp, 0.dp)
                    ) {
                        Text("Square Generator")
                    }
                }
                Row() {
                    Button(
                        onClick = {
                            settingsMain = true
                            selectGenerator = false
                            settingsLines = 2
                        },
                        modifier = Modifier
                            .offset(50.dp, 50.dp)
                    ) {
                        Text("Hectagon Generator")
                    }
                }
                Row() {
                    Button(
                        onClick = {
                            settingsMain = true
                            selectGenerator = false
                            settingsLines = 2
                        },
                        modifier = Modifier
                            .offset(50.dp, 100.dp)
                    ) {
                        Text("Pentagon Generator")
                    }
                }
                Row() {
                    Button(
                        onClick = {
                            settingsMain = true
                            selectGenerator = false
                            settingsLines = 2
                        },
                        modifier = Modifier
                            .offset(50.dp, 150.dp)
                    ) {
                        Text("Octagon Generator")
                    }
                }
                Row() {
                    Button(
                        onClick = {
                            settingsMain = true
                            selectGenerator = false
                            settingsLines = 2
                        },
                        modifier = Modifier
                            .offset(50.dp, 200.dp)
                    ) {
                        Text("Triangle Generator")
                    }
                }
            }
        }// End of the card 2.0
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