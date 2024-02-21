package com.example.hw5

import android.icu.text.CaseMap.Title
import android.icu.util.IslamicCalendar.CalculationType
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.example.hw5.ui.theme.Hw5Theme
import androidx.compose.material3.DropdownMenuItem;



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Hw5Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CaloriesScreen()
                }
            }
        }
    }
}

@Composable
fun CaloriesScreen(){
    var weightInput by remember { mutableStateOf("") }
    var weight = weightInput.toIntOrNull() ?: 0
    var male by remember { mutableStateOf(true) }
    var intensity by remember { mutableStateOf(1.3f) }
    var result by remember { mutableStateOf(0)}
    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Heading(title = stringResource(R.string.calories))
        WeightField(weightInput = weightInput, onValueChange = {weightInput = it})
        GenderChoices(male, setGenderMale = {male = it})
        IntensityList(onClick = {intensity = it})
        Text(text = result.toString(), color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold)
        Calculation(male = male, weight = weight, intensity = intensity, setResult = {result = it})
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightField(weightInput: String,onValueChange:(String) -> Unit) {
    OutlinedTextField(
        value = weightInput,
        onValueChange = onValueChange,
        label = { Text(text = "Enter weight")},
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GenderChoices(male: Boolean, setGenderMale:(Boolean) -> Unit) {
    Column(Modifier.selectableGroup()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = male,
                onClick = {setGenderMale(true)}
            )
            Text(text = "Male")
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = !male,
                onClick = {setGenderMale(false)}
            )
            Text(text = "Female")
        }
    }
}

@Composable
fun Heading(title: String) {
    Text(
        text = title,
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 16.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IntensityList(onClick:(Float) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Light") }
    var textFileSize by remember { mutableStateOf(Size.Zero) }
    val items = listOf("Light", "Usual", "Moderate", "Hard", "Very Hard")

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedTextField(
            readOnly = true,
            value = selectedText,
            onValueChange = { selectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    textFileSize = coordinates.size.toSize()
                },
            label = { Text("Selected intensity") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { expanded = !expanded })
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFileSize.width.toDp() })
        ) {
            items.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedText = label
                    val intensity = when (label) {
                        "Light" -> 1.3f
                        "Usual" -> 1.5f
                        "Moderate" -> 1.7f
                        "Hard" -> 2f
                        "Very Hard" -> 2.2f
                        else -> 0.0f
                    }
                    onClick(intensity)
                    expanded = false
                }) {
                    Text(text = label)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuItem(onClick: () -> Unit, interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }, text: @Composable () -> Unit) {

}


@Composable
fun Calculation(male: Boolean, weight: Int, intensity: Float, setResult:(Int) -> Unit ) {
    Button(
        onClick = {
            if (male) {
                setResult(((879 + 10.2 * weight) * intensity).toInt())
            } else {
                setResult(((795 + 7.18 * weight) * intensity).toInt())
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "CALCULATE")
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Hw5Theme {
        CaloriesScreen()
    }
}