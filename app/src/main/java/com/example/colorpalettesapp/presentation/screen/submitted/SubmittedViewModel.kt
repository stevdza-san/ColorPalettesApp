package com.example.colorpalettesapp.presentation.screen.submitted

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backendless.Backendless
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.domain.repository.Repository
import com.example.colorpalettesapp.util.RequestState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubmittedViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    private val _submittedPalettes = mutableStateListOf<ColorPalette>()
    val submittedPalettes: List<ColorPalette> = _submittedPalettes

    var requestState by mutableStateOf<RequestState>(RequestState.Idle)
        private set

    init {
        getSubmittedPalettes()
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeSubmittedPalettes(
                userObjectId = Backendless.UserService.CurrentUser().objectId
            ).collect { palette ->
                _submittedPalettes.add(palette)
            }
        }
    }

    private fun getSubmittedPalettes() {
        requestState = RequestState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = _submittedPalettes.addAll(
                repository.getSubmittedPalettes(
                    userObjectId = Backendless.UserService.CurrentUser().objectId
                )
            )
            requestState =
                if (result) RequestState.Success
                else RequestState.Error
        }
    }

}