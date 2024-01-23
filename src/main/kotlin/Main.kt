import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.DpOffset
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
    var settingsLines by remember { mutableStateOf(1) }
    var menuLines by remember { mutableStateOf(2) }
    var compactExportToggle by remember { mutableStateOf(compactExport) }
    var genBtnState by remember { mutableStateOf(false) }

    // Testing Stuff
    var caption by remember { mutableStateOf("theres NO PICTURE") }

    // holy mess
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var displayed by remember { mutableStateOf(false) }

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
//                .offset((if(menuCardState) (if (!settingsCardState) 10 else 320) else (-310)).dp,50.dp),
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