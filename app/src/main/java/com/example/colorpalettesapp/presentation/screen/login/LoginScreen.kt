package com.example.colorpalettesapp.presentation.screen.login

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.colorpalettesapp.navigation.Screen
import com.example.colorpalettesapp.presentation.screen.StartActivityForResult
import com.example.colorpalettesapp.presentation.screen.signIn

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val signedInState = loginViewModel.signedInState
    val messageBarState = loginViewModel.messageBarState

    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            LoginTopBar()
        },
        content = {
            LoginContent(
                signedInState = signedInState,
                messageBarState = messageBarState,
                onButtonClicked = {
                    loginViewModel.updateSignedInState(signedIn = true)
                }
            )
        }
    )

    StartActivityForResult(
        key = signedInState,
        onResultReceived = { accessToken ->
            Backendless.UserService.loginWithOAuth2(
                "googleplus",
                accessToken,
                mutableMapOf(
                    "email" to "email",
                    "given_name" to "first_name",
                    "family_name" to "last_name",
                    "picture" to "profile_photo"
                ),
                object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        navController.popBackStack()
                        navController.navigate(Screen.Home.route)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        loginViewModel.updateSignedInState(signedIn = false)
                        fault?.let { loginViewModel.updateMessageBarState(message = it.message) }
                    }
                },
                false
            )
        },
        onDialogDismissed = {
            loginViewModel.updateSignedInState(signedIn = false)
            loginViewModel.updateMessageBarState(message = it)
        },
        launcher = { activityLauncher ->
            if (signedInState) {
                signIn(activity = activity, launcher = activityLauncher)
            }
        }
    )
}