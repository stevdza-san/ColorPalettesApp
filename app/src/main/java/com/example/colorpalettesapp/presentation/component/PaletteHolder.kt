package com.example.colorpalettesapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.presentation.screen.extractColors
import com.example.colorpalettesapp.presentation.screen.hexToColor

@Composable
fun PaletteHolder(
    colorPalette: ColorPalette,
    onClick: () -> Unit
) {
    val colors = remember { mutableStateListOf<String>() }
    LaunchedEffect(key1 = colorPalette) {
        colors.clear()
        colors.addAll(extractColors(colorPalette = colorPalette))
    }

    Box(
        modifier = Modifier
            .height(200.dp)
            .fillMaxWidth()
            .clickable(enabled = colorPalette.approved) {
                onClick()
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        ColorPalette(colors = colors)
        NumberOfLikes(number = colorPalette.totalLikes ?: 0)
        if (!colorPalette.approved) {
            WaitingForApproval()
        }
    }
}

@Composable
fun ColorPalette(colors: List<String>) {
    val width = remember { mutableStateOf(0) }
    val weight = remember {
        if (width.value != 0) (colors.size / width.value).toFloat()
        else 1f
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned {
                width.value = it.size.width
            }
            .clip(RoundedCornerShape(size = 20.dp))
    ) {
        colors.forEach { colorHex ->
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(weight)
                    .background(color = hexToColor(colorHex = colorHex))
            )
        }
    }
}

@Composable
fun NumberOfLikes(number: Int) {
    Surface(
        modifier = Modifier
            .padding(end = 12.dp, bottom = 12.dp),
        shape = RoundedCornerShape(size = 50.dp),
        color = Color.Black.copy(alpha = ContentAlpha.disabled)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = 6.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Heart Icon",
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "$number",
                color = Color.White
            )
        }
    }
}

@Composable
fun WaitingForApproval() {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(size = 20.dp))
            .background(Color.Black.copy(alpha = ContentAlpha.medium)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Filled.HourglassBottom,
            contentDescription = "Hourglass Icon",
            tint = Color.White
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = "Waiting for approval",
            color = Color.White
        )
    }
}