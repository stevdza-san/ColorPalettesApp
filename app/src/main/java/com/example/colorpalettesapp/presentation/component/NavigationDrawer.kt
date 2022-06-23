package com.example.colorpalettesapp.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.colorpalettesapp.domain.model.DrawerItem
import com.example.colorpalettesapp.navigation.Screen
import com.example.colorpalettesapp.presentation.screen.logout
import com.example.colorpalettesapp.ui.theme.InfoGreen
import kotlinx.coroutines.launch

@Composable
fun NavigationDrawer(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    logoutFailed: () -> Unit
) {
    DrawerHeader()
    DrawerBody(
        navController = navController,
        scaffoldState = scaffoldState,
        logoutFailed = logoutFailed
    )
}

@Composable
fun DrawerHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Coolors",
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.h5.fontSize
        )
        Text(text = "For Designers")
    }
}

@Composable
fun DrawerBody(
    navController: NavHostController,
    scaffoldState: ScaffoldState,
    logoutFailed: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()

    val drawerItems = listOf(
        DrawerItem.Home,
        DrawerItem.Saved,
        DrawerItem.Submitted,
        DrawerItem.Logout
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        drawerItems.forEach { drawerItem ->
            DrawerItem(
                drawerItem = drawerItem,
                selected = currentRoute == drawerItem.route,
                onClick = {
                    if (currentRoute != drawerItem.route && drawerItem != DrawerItem.Logout) {
                        navController.navigate(drawerItem.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                    if (drawerItem == DrawerItem.Logout) {
                        if (drawerItem == DrawerItem.Logout) {
                            logout(
                                onSuccess = {
                                    navController.popBackStack()
                                    navController.navigate(
                                        Screen.Login.passSignedInState(
                                            signedInState = false
                                        )
                                    )
                                },
                                onFailed = logoutFailed
                            )
                        }
                    }
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        }
    }
}

@Composable
fun DrawerItem(
    drawerItem: DrawerItem,
    selected: Boolean,
    itemColor: Color =
        if (selected) InfoGreen
        else MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium),
    backgroundColor: Color =
        if (selected) MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
        else Color.Transparent,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(color = backgroundColor)
            .height(48.dp)
            .padding(vertical = 12.dp)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = drawerItem.icon,
            contentDescription = "Drawer icon",
            tint = itemColor
        )
        Spacer(modifier = Modifier.width(32.dp))
        Text(text = drawerItem.title, color = itemColor)
    }
}

@Composable
@Preview(showBackground = true)
fun DrawerHeaderPreview() {
    DrawerHeader()
}

@Composable
@Preview(showBackground = true)
fun DrawerBodyPreview() {
    DrawerBody(
        navController = rememberNavController(),
        scaffoldState = rememberScaffoldState(),
        logoutFailed = {}
    )
}