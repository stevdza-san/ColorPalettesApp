package com.example.colorpalettesapp.presentation.screen

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.util.Constants.CLIENT_ID
import com.example.colorpalettesapp.util.Constants.CLIENT_SECRET
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun StartActivityForResult(
    key: Any,
    onResultReceived: (String?) -> Unit,
    onDialogDismissed: (String) -> Unit,
    launcher: (ManagedActivityResultLauncher<Intent, ActivityResult>) -> Unit
) {
    val scope = rememberCoroutineScope()
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { activityResult ->
        try {
            if (activityResult.resultCode == Activity.RESULT_OK) {
                val result = activityResult.data?.let { intent ->
                    Auth.GoogleSignInApi.getSignInResultFromIntent(intent)
                }
                val serverAuthCode = result?.signInAccount?.serverAuthCode
                Log.d("StartActivityForResult", "ACCESS TOKEN: $serverAuthCode")
                scope.launch(Dispatchers.IO) {
                    onResultReceived(getAccessToken(authCode = serverAuthCode))
                }
            } else {
                onDialogDismissed("Result not okay")
                Log.d("StartActivityForResult", "RESULT NOT OK")
            }
        } catch (e: Exception) {
            onDialogDismissed(e.message.toString())
            Log.d("StartActivityForResult", "${e.message}")
        }
    }

    LaunchedEffect(key1 = key) {
        launcher(activityLauncher)
    }
}

private fun getAccessToken(authCode: String?): String? {
    val tokenResponse: GoogleTokenResponse = try {
        GoogleAuthorizationCodeTokenRequest(
            NetHttpTransport(),
            GsonFactory(),
            "https://www.googleapis.com/oauth2/v4/token",
            CLIENT_ID,
            CLIENT_SECRET,
            authCode,
            "https://eu-api.backendless.com/195055C2-93D4-F6AB-FF9C-142C8A8E2500/268496DA-6899-4519-ABBD-39E77BB93957/users/oauth/googleplus/authorize"
        ).execute()
    } catch (e: Exception) {
        Log.d("getAccessToken", e.message.toString())
        return null
    }
    return tokenResponse.accessToken
}

fun signIn(
    activity: Activity,
    launcher: ManagedActivityResultLauncher<Intent, ActivityResult>
) {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestServerAuthCode(CLIENT_ID)
        .build()
    val client = GoogleSignIn.getClient(activity, gso)

    launcher.launch(client.signInIntent)
}

fun logout(onSuccess: () -> Unit, onFailed: () -> Unit) {
    Backendless.UserService.logout(
        object : AsyncCallback<Void> {
            override fun handleResponse(response: Void?) {
                Log.d("Logout", "Success")
                onSuccess()
            }

            override fun handleFault(fault: BackendlessFault?) {
                Log.d("Logout", fault?.message.toString())
                onFailed()
            }
        }
    )
}

fun extractColors(colorPalette: ColorPalette?): List<String> {
    val colors = mutableListOf<String>()
    colorPalette?.colors?.split(",")?.forEach {
        colors.add(it.trim())
    }
    return colors
}

fun hexToColor(colorHex: String): Color {
    return try {
        Color(("FF" + colorHex.removePrefix("#")).toLong(16))
    } catch (e: Exception) {
        Color("FFFFFFFF".toLong(16))
    }
}

fun parseErrorMessage(message: String): String {
    return if (message.contains("No address associated with hostname")) {
        "No Internet Connection"
    } else {
        message
    }
}