package com.example.colorpalettesapp.presentation.screen.details

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.colorpalettesapp.ui.theme.BookmarkYellow
import com.example.colorpalettesapp.ui.theme.topAppBarBackgroundColor
import com.example.colorpalettesapp.ui.theme.topAppBarContentColor

@Composable
fun DetailsTopBar(
    isSaved: Boolean,
    onBackClicked: () -> Unit,
    onSaveClicked: () -> Unit
) {
    TopAppBar(
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back Arrow Icon",
                    tint = MaterialTheme.colors.topAppBarContentColor
                )
            }
        },
        title = {
            Text(
                text = "Details",
                color = MaterialTheme.colors.topAppBarContentColor
            )
        },
        actions = {
            IconButton(onClick = onSaveClicked) {
                Icon(
                    imageVector = Icons.Filled.Bookmark,
                    contentDescription = "Bookmark Icon",
                    tint = if (isSaved) BookmarkYellow
                    else MaterialTheme.colors.topAppBarContentColor
                )
            }
        }
    )
}

@Composable
@Preview
fun DetailsTopBarSavedPreview() {
    DetailsTopBar(
        isSaved = true,
        onBackClicked = {},
        onSaveClicked = {}
    )
}

@Composable
@Preview
fun DetailsTopBarPreview() {
    DetailsTopBar(
        isSaved = false,
        onBackClicked = {},
        onSaveClicked = {}
    )
}