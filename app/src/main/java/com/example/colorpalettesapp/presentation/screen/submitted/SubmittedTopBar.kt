package com.example.colorpalettesapp.presentation.screen.submitted

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.ui.theme.topAppBarBackgroundColor
import com.example.colorpalettesapp.ui.theme.topAppBarContentColor
import com.example.colorpalettesapp.util.RequestState

@Composable
fun SubmittedTopBar(
    submittedColors: List<ColorPalette>,
    requestState: RequestState,
    onSubmitClicked: () -> Unit,
    onMenuClicked: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        navigationIcon = {
            IconButton(onClick = onMenuClicked) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Drawer Icon",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        title = {
            if (requestState is RequestState.Success ||
                requestState is RequestState.Error
            ) {
                Text(
                    text = if (submittedColors.isEmpty()) "Submit"
                    else "Submitted Palettes",
                    color = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        actions = {
            IconButton(onClick = onSubmitClicked) {
                Icon(
                    imageVector = Icons.Filled.CloudUpload,
                    contentDescription = "Submit Icon",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        }
    )
}

@Composable
@Preview
fun SubmitTopBarPreview() {
    SubmittedTopBar(
        submittedColors = listOf(),
        requestState = RequestState.Success,
        onSubmitClicked = {},
        onMenuClicked = {}
    )
}

@Composable
@Preview
fun SubmitTopBarColorsPreview() {
    SubmittedTopBar(
        submittedColors = listOf(ColorPalette()),
        requestState = RequestState.Success,
        onSubmitClicked = {},
        onMenuClicked = {}
    )
}