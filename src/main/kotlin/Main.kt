import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.sharp.Build
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import utils.GenerateNodes
import utils.NodeGeneratorType

@Composable
@Preview
fun App() {
    var genBtnState by mutableStateOf(false)

    Scaffold(
        topBar = {
            TopAppBar (
                title = {
                    Text("Image Crunch α")
                },

                actions = {
                    IconButton(onClick = { /* Do shit */}) {
                        Icon(
                            imageVector = Icons.Sharp.Build,
                            contentDescription = "Settings"
                        )
                    }

                    IconButton(onClick = { /* Do shit */}) {
                        Icon(
                            imageVector = Icons.Sharp.Edit,
                            contentDescription = "Test Text"
                        )
                    }

                    IconButton(onClick = { /* Do shit */}) {
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

fun main() = application {
    val state = rememberWindowState(
        size = DpSize(1200.dp, 800.dp),
        position = WindowPosition(300.dp, 300.dp)
    )

    Window(title = "Image Crunch v0.1.a", onCloseRequest = ::exitApplication, state = state) {
        App()
    }
}
