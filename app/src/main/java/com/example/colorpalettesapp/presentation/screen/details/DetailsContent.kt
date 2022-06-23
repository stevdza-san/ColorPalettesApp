package com.example.colorpalettesapp.presentation.screen.details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.presentation.screen.extractColors
import com.example.colorpalettesapp.presentation.screen.hexToColor

@Composable
fun DetailsContent(
    colorPalette: ColorPalette,
    onColorClicked: (String) -> Unit
) {
    val colors = remember(key1 = colorPalette) {
        mutableStateOf(extractColors(colorPalette = colorPalette))
    }

    val height = remember { mutableStateOf(0) }
    val weight = remember {
        if (height.value != 0) (colors.value.size / height.value).toFloat()
        else 1f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                height.value = it.size.height
            }
    ) {
        colors.value.forEach { colorHex ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight)
                    .background(color = hexToColor(colorHex = colorHex))
                    .clickable { onColorClicked(colorHex) },
                contentAlignment = Alignment.Center
            ) {
                Surface(color = Color.Transparent) {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .padding(vertical = 6.dp),
                        text = colorHex,
                        color = Color.White
                    )
                }
            }
        }
    }
}