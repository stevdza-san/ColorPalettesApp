package com.example.colorpalettesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.backendless.Backendless
import com.example.colorpalettesapp.navigation.SetupNavGraph
import com.example.colorpalettesapp.presentation.screen.logout
import com.example.colorpalettesapp.ui.theme.ColorPalettesAppTheme
import com.example.colorpalettesapp.util.Constants.API_KEY
import com.example.colorpalettesapp.util.Constants.APP_ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onResume() {
        super.onResume()
        logout(onSuccess = {}, onFailed = {})
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Backendless.initApp(this, APP_ID, API_KEY)
        Backendless.setUrl("https://eu-api.backendless.com")

        setContent {
            ColorPalettesAppTheme {
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }
}