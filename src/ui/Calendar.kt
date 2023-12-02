package days.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
data class DayWindow(val id: Int, val title: String, val enabled: Boolean)

fun getTitle(id: Int): String =
        when (id) {
            1 -> "Trebuchet?!"
            2 -> "Cube Conundrum"
            else -> "?"
        }

fun isEnabled(id: Int): Boolean =
        when (id) {
            1, 2 -> true
            else -> false
        }

val dayList = buildList {
    repeat(25) {
        add(DayWindow(it + 1, getTitle(it + 1), isEnabled(it + 1)))
    }
}

@Composable
fun Calendar(modifier: Modifier = Modifier) {
    var currentId by remember { mutableStateOf(0) }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Top) {
        Button(onClick = { currentId = 0 }) {
            Text("Home")
        }
        when (currentId) {
            0 -> {
                Home(changeState = { id -> currentId = id })
            }

            else -> {
                Box(modifier = Modifier.background(Color.Yellow).fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Home(changeState: (Int) -> Unit, modifier: Modifier = Modifier) {
    FlowRow(
            maxItemsInEachRow = 5,
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        for (d in dayList) {
            if (d.enabled) {
                BrightBox(d, Modifier.size(100.dp).clickable { changeState(d.id) }.padding(4.dp))
            } else {
                GreyBox(d, Modifier.size(100.dp).padding(4.dp))
            }
        }
    }

}

@Composable
fun BrightBox(dayWindow: DayWindow, modifier: Modifier = Modifier) {
    val boxColor = if (dayWindow.id % 2 == 0) Color.Red else Color.Green
    Box(modifier.aspectRatio(1.0f).background(boxColor), contentAlignment = Alignment.Center) {
        Text(dayWindow.id.toString())
    }
}

@Composable
fun GreyBox(dayWindow: DayWindow, modifier: Modifier = Modifier) {
    Box(modifier.aspectRatio(1.0f).background(Color.Gray), contentAlignment = Alignment.Center) {
        Text(dayWindow.id.toString())
    }
}

