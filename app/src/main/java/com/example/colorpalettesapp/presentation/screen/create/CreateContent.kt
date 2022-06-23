package com.example.colorpalettesapp.presentation.screen.create

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.colorpalettesapp.presentation.component.ColorPicker
import com.example.colorpalettesapp.ui.theme.InfoGreen

@Composable
fun CreateContent(onSubmitClicked: (String) -> Unit) {
    val selectedColors = remember { mutableStateListOf<String?>(null, null, null, null, null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(60.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = "Add Colors",
            style = TextStyle(
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
        )
        Spacer(modifier = Modifier.height(6.dp))
        repeat(times = 5) { index ->
            ColorPicker(selectedColor = { color ->
                selectedColors.set(index = index, element = "#${color?.takeLast(n = 6)}")
            })
        }
        Spacer(modifier = Modifier.height(6.dp))
        Button(
            enabled = selectedColors.filter { it == null }.size <= 2,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = InfoGreen
            ),
            onClick = {
                val parsedColors = selectedColors.toMutableList()
                parsedColors.removeAll { it == null }
                onSubmitClicked(parsedColors.joinToString(separator = ","))
            }
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = "Check Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = "Submit", color = Color.White)
        }
    }
}