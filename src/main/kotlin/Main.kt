/**
 * @author Alexander Yauchler
 * @author Aidan Mao
 *
 * Project Timeline : 1/17/2024 -> TBD (3/28/2025)
 *
 * This software is licensed under the MIT License
 *
 * Written by Aidan Mao and Alexander Yauchler
 * Freshmen in Computer Science at Purdue University Fort Wayne
 */

import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.konyaco.fluent.icons.Icons
import com.konyaco.fluent.icons.filled.ImageEdit
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.intui.standalone.theme.*
import org.jetbrains.jewel.intui.window.decoratedWindow
import org.jetbrains.jewel.intui.window.styling.dark
import org.jetbrains.jewel.ui.ComponentStyling
import org.jetbrains.jewel.ui.component.Dropdown
import org.jetbrains.jewel.window.DecoratedWindow
import org.jetbrains.jewel.window.TitleBar
import org.jetbrains.jewel.window.newFullscreenControls
import org.jetbrains.jewel.window.styling.TitleBarStyle
import ui.Colors
import ui.ViewModel
import ui.app
//import ui.launchThemes

fun main() {
//    ViewModel.loadedThemes = launchThemes()
//    ViewModel.themeColor = ViewModel.loadedThemes[0].exportData()

        application {
        val state = rememberWindowState(
            size = DpSize(1200.dp, 800.dp),
            position = WindowPosition(150.dp, 150.dp)
        )

        IntUiTheme (
            theme = JewelTheme.darkThemeDefinition(
                defaultTextStyle = JewelTheme.createDefaultTextStyle(),
                editorTextStyle = JewelTheme.createEditorTextStyle()
            ),
            styling = ComponentStyling.default().decoratedWindow(
                titleBarStyle = TitleBarStyle.dark()
            ),
        ) {
            DecoratedWindow(
                onCloseRequest = { exitApplication() },
                title = "Image Crunch v1.0.2a",
                state = state,
                content = {
                    TitleBar(Modifier.newFullscreenControls()) {
                        Row(
                            Modifier.align(Alignment.Start),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                Icons.Filled.ImageEdit,
                                "Image Crunch Icon",
                                modifier = Modifier.size(20.dp),
                                Colors.AccentBlue
                            )
                        }

                        Row(
                            Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Dropdown (
                                menuContent = {
                                    ViewModel.projectList.forEach { project ->
                                        selectableItem(
                                            selected = ViewModel.currentProject.value == project,
                                            onClick = { ViewModel.currentProject.value = project },
                                        ) {
                                            Text(project.first)
                                        }
                                    }
                                },
                            ) {
                                Text(ViewModel.currentProject.value.first)
                            }
                        }

                        Row(
                            Modifier.align(Alignment.End),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                "v1.0.2a",
                                color = Colors.HiddenGray,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }

                    app()
                },
            )
        }
    }
}