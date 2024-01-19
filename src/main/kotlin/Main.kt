import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.List
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.launch
import utils.GenerateNodes
import utils.NodeGeneratorType
import utils.ImageFileSelection
import javax.imageio.ImageIO
import java.io.File

@Composable
@Preview
fun App() {
    var genBtnState by mutableStateOf(false)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
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
                        scope.launch {
                            snackbarHostState.showSnackbar("Edit Pressed")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.Edit,
                            contentDescription = "Test Text"
                        )
                    }

                    IconButton(onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("List Pressed")
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Sharp.List,
                            contentDescription = "Test Text"
                        )
                    }
                }
            )
        }
    ) {
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
