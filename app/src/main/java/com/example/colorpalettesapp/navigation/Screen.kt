package com.example.colorpalettesapp.navigation

sealed class Screen(val route: String) {
    object Login : Screen(route = "login/{signedInState}") {
        fun passSignedInState(signedInState: Boolean = true) =
            "login/$signedInState"
    }

    object Home : Screen(route = "home")
    object Details : Screen(route = "details/{showFab}") {
        fun passShowFab(showFab: Boolean = true) =
            "details/$showFab"
    }

    object Saved : Screen(route = "saved")
    object Submitted : Screen(route = "submitted")
    object Create : Screen(route = "create")
}
