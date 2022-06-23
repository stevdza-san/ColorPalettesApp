package com.example.colorpalettesapp.presentation.screen.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.domain.repository.Repository
import com.example.colorpalettesapp.presentation.screen.parseErrorMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val _uiEvent = Channel<CreateScreenUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun submitColorPalette(colorPalette: ColorPalette) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.submitColorPalette(colorPalette = colorPalette)
                _uiEvent.send(CreateScreenUiEvent.Submitted)
            } catch (e: Exception) {
                _uiEvent.send(
                    CreateScreenUiEvent.Error(
                        error = parseErrorMessage(message = e.message.toString())
                    )
                )
            }
        }
    }

}

sealed class CreateScreenUiEvent(val message: String) {
    object Submitted : CreateScreenUiEvent(message = "Successfully Submitted!")
    data class Error(val error: String) : CreateScreenUiEvent(message = error)
}