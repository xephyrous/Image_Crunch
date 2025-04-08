package ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.konyaco.fluent.FluentTheme
import com.konyaco.fluent.component.SideNav
import com.konyaco.fluent.icons.Icons
import com.konyaco.fluent.icons.filled.Options
import com.konyaco.fluent.icons.filled.PaintBrush
import com.konyaco.fluent.icons.filled.Settings
import com.konyaco.fluent.surface.Card
import ui.components.ComponentItem
import ui.components.NavigationItem

@Composable
fun app() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2b2d30))
    ) {
        // Top bar
        Box (
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.05f)
                .padding(5.dp)
        ) {
            Card (
                modifier = Modifier
                    .fillMaxSize(),
                RoundedCornerShape(5.dp)
            ) { }
        }

        // Side Navigation Bar
        var navExpanded by remember { mutableStateOf(false) }
        val navigationOptions = listOf(
            ComponentItem(
                "App Settings",
                "",
                null,
                Icons.Filled.Settings,
            ) { },
            ComponentItem(
                "Generator Settings",
                "",
                null,
                Icons.Filled.Options,
            ) { },
            ComponentItem(
                "Themes",
                "",
                null,
                Icons.Filled.PaintBrush,
            ) { }
        )
        val selectedItem = remember { mutableStateOf(navigationOptions.first()) }

        SideNav (
            modifier = Modifier
                .fillMaxHeight(0.65f),
            expanded = navExpanded,
            onExpandStateChange = { navExpanded = it }
        ) {
            navigationOptions.forEach { navItem ->
                NavigationItem(selectedItem.value, {}, navItem)
            }
        }

        // Bottom Settings Bar
        Card (
            modifier = Modifier
                .fillMaxSize()
                .padding(5.dp),
            RoundedCornerShape(5.dp)
        ) {

        }
    }
}