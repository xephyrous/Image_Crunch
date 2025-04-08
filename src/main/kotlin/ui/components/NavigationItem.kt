package ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.konyaco.fluent.component.Icon
import com.konyaco.fluent.component.SideNavItem
import com.konyaco.fluent.component.Text

@Composable
fun NavigationItem(
    selectedItem: ComponentItem,
    onSelectedItemChange: (ComponentItem) -> Unit,
    navItem: ComponentItem,
) {
    SideNavItem(
        selectedItem == navItem,
        onClick = {
            onSelectedItemChange(navItem)
        },
        icon = navItem.icon?.let { { Icon(it, navItem.name) } },
        content = { Text(navItem.name) },
        items = {
            navItem.items?.let {
                if (it.isNotEmpty()) {
                    it.forEach { item ->
                        NavigationItem(
                            selectedItem = selectedItem,
                            onSelectedItemChange = onSelectedItemChange,
                            navItem = item,
                        )
                    }
                }
            }
        }
    )
}