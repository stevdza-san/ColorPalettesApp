package com.example.colorpalettesapp.util

sealed class RequestState {
    object Idle: RequestState()
    object Loading: RequestState()
    object Success: RequestState()
    object Error: RequestState()
}
