package com.example.colorpalettesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.presentation.screen.create.CreateScreen
import com.example.colorpalettesapp.presentation.screen.details.DetailsScreen
import com.example.colorpalettesapp.presentation.screen.home.HomeScreen
import com.example.colorpalettesapp.presentation.screen.login.LoginScreen
import com.example.colorpalettesapp.presentation.screen.saved.SavedScreen
import com.example.colorpalettesapp.presentation.screen.submitted.SubmittedScreen
import com.example.colorpalettesapp.util.Constants.SELECTED_PALETTE_KEY

@Composable
fun SetupNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(
            route = Screen.Login.route,
            arguments = listOf(navArgument(name = "signedInState", builder = {
                type = NavType.BoolType
                defaultValue = true
            }))
        ) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Details.route,
            arguments = listOf(navArgument(name = "showFab", builder = {
                type = NavType.BoolType
                defaultValue = true
            }))
        ) {
            val selectedPalette =
                navController.previousBackStackEntry?.savedStateHandle?.get<ColorPalette>(
                    key = SELECTED_PALETTE_KEY
                )
            if (selectedPalette != null) {
                DetailsScreen(
                    navController = navController,
                    colorPalette = selectedPalette,
                    showFab = it.arguments?.getBoolean("showFab") ?: true
                )
            }
        }
        composable(route = Screen.Saved.route) {
            SavedScreen(navController = navController)
        }
        composable(route = Screen.Submitted.route) {
            SubmittedScreen(navController = navController)
        }
        composable(route = Screen.Create.route) {
            CreateScreen(navController = navController)
        }
    }
}