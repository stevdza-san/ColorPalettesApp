package com.example.colorpalettesapp.presentation.screen.saved

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.colorpalettesapp.presentation.component.NavigationDrawer
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SavedScreen(
    navController: NavHostController,
    savedViewModel: SavedViewModel = hiltViewModel()
) {
    val savedPalettes = savedViewModel.savedPalettes
    val requestState = savedViewModel.requestState

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SavedTopBar(onMenuClicked = {
                scope.launch { scaffoldState.drawerState.open() }
            })
        },
        drawerContent = {
            NavigationDrawer(
                navController = navController,
                scaffoldState = scaffoldState,
                logoutFailed = {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar(
                            message = "Something went wrong",
                            actionLabel = "OK"
                        )
                    }
                }
            )
        },
        content = {
            SavedContent(
                navController = navController,
                requestState = requestState,
                savedPalette = savedPalettes
            )
        }
    )

}