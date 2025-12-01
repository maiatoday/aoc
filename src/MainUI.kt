package days

import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(width = 1000.dp, height = 1000.dp),
        alwaysOnTop = true,
    ) {
        BasicText("Hello AoC ⭐️")
    }
}