package com.example.colorpalettesapp.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Logout
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.colorpalettesapp.navigation.Screen

sealed class DrawerItem(
    val icon: ImageVector,
    val title: String,
    val route: String
) {
    object Home: DrawerItem(
        icon = Icons.Default.Home,
        title = "Home",
        route = Screen.Home.route
    )
    object Saved: DrawerItem(
        icon = Icons.Default.Bookmark,
        title = "Saved Palettes",
        route = Screen.Saved.route
    )
    object Submitted: DrawerItem(
        icon = Icons.Default.CloudUpload,
        title = "Submit a Palette",
        route = Screen.Submitted.route
    )
    object Logout: DrawerItem(
        icon = Icons.Default.Logout,
        title = "Logout",
        route = Screen.Login.route
    )
}
