package com.example.colorpalettesapp.presentation.screen.create

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.colorpalettesapp.domain.model.ColorPalette

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateScreen(
    navController: NavHostController,
    createViewModel: CreateViewModel = hiltViewModel()
) {
    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = Unit) {
        createViewModel.uiEvent.collect {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it.message,
                actionLabel = "OK"
            )
        }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CreateTopBar(onBackClicked = {
                navController.popBackStack()
            })
        },
        content = {
            CreateContent(
                onSubmitClicked = {
                    createViewModel.submitColorPalette(
                        colorPalette = ColorPalette(colors = it)
                    )
                }
            )
        }
    )
}