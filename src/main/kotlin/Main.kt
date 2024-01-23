import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import utils.*
import java.awt.Dimension
import java.io.File
import java.io.FileInputStream

@Composable
@Preview
fun App() {
    // Settings
    var compactExportToggle by remember { mutableStateOf(compactExport) }
    var genBtnState by remember { mutableStateOf(false) }

    // Testing Stuff
    var caption by remember { mutableStateOf("theres NO PICTURE") }

    // holy mess
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var displayed by remember { mutableStateOf(false) }

    // moves the cards on so its more responsive (i guess)
    var menuCardState by remember { mutableStateOf(false) }
    var settingsCardState by remember { mutableStateOf(false) }
    val offset by animateIntOffsetAsState(
        targetValue = if (menuCardState) {
            // Temporary maybe? moves menu menu past settings menu if both are open
            if (!settingsCardState) {
                IntOffset(10, 50)
            } else {
                IntOffset(610, 50)
            }
        } else {
            IntOffset(-700, 50)
        },
        label = "offset"
    )

    // change this if u dare
    val owoffset by animateIntOffsetAsState(
        targetValue = if (settingsCardState) {
            IntOffset(10, 50)
        } else {
            IntOffset(-700, 50)
        },
        label = "owoffset"
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
                // how do i scale the size of this?!?!!??!
                .size(300.dp, 600.dp)
                .offset { offset },
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
                    }
                )
                Text(compactExport.toString())
                Button(
                    onClick = {
                        if (compactExport) {
                            settingsToString()
                        } else {
                            settingsToCSV()
                        }
                    },
                    modifier = Modifier.offset { IntOffset(5, 0) }
                    ) {
                    Text("Export")
                }
            }
        }// End of the card

        Card(
            modifier = Modifier
                // how do i scale the size of this?!?!!??!
                .size(300.dp, 600.dp)
                .offset { owoffset },
            elevation = 20.dp
        ) {
            // TODO: WHAT THE FLUFF IS A SETTINGS
            // settings menu????!?!!!
            Row() {
                Text(text = "this is supposed to be a settings menu :O")

                IconButton(onClick = {
                    // There is no settings
                }) {
                    Icon(
                        imageVector = Icons.Sharp.AddCircle,
                        contentDescription = "Test Text"
                    )
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