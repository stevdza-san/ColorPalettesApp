package com.example.colorpalettesapp.presentation.screen.saved

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorpalettesapp.ui.theme.topAppBarBackgroundColor
import com.example.colorpalettesapp.ui.theme.topAppBarContentColor

@Composable
fun SavedTopBar(onMenuClicked: () -> Unit) {
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
            Text(
                text = "Saved",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        }
    )
}

@Composable
@Preview
fun SavedTopBarPreview() {
    SavedTopBar {}
}