import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import utils.GenerateNodes
import utils.NodeGeneratorType
import utils.ImageFileSelection
import java.io.File

@Composable
@Preview
fun App() {
    var genBtnState by mutableStateOf(false)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var cardState by remember { mutableStateOf(false) }
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
                    IconButton(onClick = {
                        selectedImage = ImageFileSelection()

                        if (selectedImage == null) {
                            scope.launch {
                                snackbarHostState.showSnackbar("Failed to Load")
                            }
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar("Image Loaded")
                            }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.Add,
                            contentDescription = "Select Image"
                        )
                    }
                    
                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Settings Clicked")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.Build,
                            contentDescription = "Settings"
                        )
                    }
                    IconButton(onClick = {
                        cardState = true
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
//            Image(
//                loadImageBitmap(inputStream = FileInputStream(selectedImage)),
//                "temp"
//            )
            Text("temp")
        }
        if(cardState) {
            Card(
                modifier = Modifier.size(300.dp, 600.dp),
                elevation = 10.dp

            ) {
                Text(text = "test")
            }
        }
    }
}

var selectedImage: File? = null

fun main() = application {
    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(300.dp, 300.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}