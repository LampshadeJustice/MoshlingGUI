import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.MipmapMode

@Composable
@Preview
fun App() {
    var text by remember { mutableStateOf("") }

    var plants by remember { mutableStateOf( Array(3) { Plant(Color.Any, PlantType.None) } ) }

    MaterialTheme {
        Column {
            Row {
                plantPicker(plants[0])
                plantPicker(plants[1])
                plantPicker(plants[2])
            }

            Button(onClick = {
                text = testRecipe(MoshlingRecipe("", plants[0], plants[1], plants[2]))
            }) {
                Text("Calculate Recipe")
            }

            Text( text = text,
                  modifier = Modifier.verticalScroll(rememberScrollState()) )
        }
    }
}

@Composable
fun plantPicker(plant: Plant) {
    var expandedColor by remember { mutableStateOf(false) }
    var expandedPlant by remember { mutableStateOf(false) }

    Row (modifier = Modifier.padding(20.dp)) {
        Box (modifier = Modifier.padding(5.dp)) {
            Text(plant.c.toString(), modifier = Modifier.clickable(onClick = { expandedColor = true }))
            DropdownMenu(
                expanded = expandedColor,
                onDismissRequest = { expandedColor = false },
            ) {
                Color.entries.forEach { s ->
                    DropdownMenuItem(onClick = {
                        plant.c = s
                        expandedColor = false
                    }) { Text(text = s.toString()) }
                }
            }
        }

        Box (modifier = Modifier.padding(5.dp)) {
            Text(plant.p.toString(), modifier = Modifier.clickable(onClick = { expandedPlant = true }))
            DropdownMenu(
                expanded = expandedPlant,
                onDismissRequest = { expandedPlant = false },
            ) {
                PlantType.entries.forEach { s ->
                    DropdownMenuItem(onClick = {
                        plant.p = s
                        expandedPlant = false
                    }) { Text(text = s.toString()) }
                }
            }
        }
    }

}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
