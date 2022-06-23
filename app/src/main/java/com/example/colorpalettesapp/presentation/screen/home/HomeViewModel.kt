package com.example.colorpalettesapp.presentation.screen.home

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.backendless.rt.data.RelationStatus
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.domain.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var colorPalettes = mutableStateListOf<ColorPalette>()
        private set

    init {
        getColorPalettes()
        observeAddRelation()
        observeDeleteRelation()
        observeApproval()
        observeDeletedPalettes()
    }

    private fun getColorPalettes() {
        viewModelScope.launch(Dispatchers.IO) {
            colorPalettes.addAll(repository.getColorPalettes())
        }
    }

    private fun observeAddRelation() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeAddRelation().collect { status ->
                updateNumberOfLikes(relationStatus = status)
            }
        }
    }

    private fun observeDeleteRelation() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeDeleteRelation().collect { status ->
                updateNumberOfLikes(relationStatus = status)
            }
        }
    }

    private suspend fun updateNumberOfLikes(relationStatus: RelationStatus?) {
        val observedPalette =
            relationStatus?.parentObjectId?.let { repository.getLikeCount(objectId = it) }

        var position = 0
        var palette = ColorPalette()
        colorPalettes.forEachIndexed { index, colorPalette ->
            if (colorPalette.objectId == relationStatus?.parentObjectId) {
                position = index
                palette = colorPalette
            }
        }
        colorPalettes.set(
            index = position,
            element = palette.copy(totalLikes = observedPalette?.totalLikes)
        )
    }

    private fun observeApproval() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeApproval().collect { palette ->
                if (palette.approved) {
                    colorPalettes.add(palette)
                } else {
                    colorPalettes.removeAll {
                        it.objectId == palette.objectId
                    }
                }
            }
        }
    }

    private fun observeDeletedPalettes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.observeDeletedPalettes().collect { palette ->
                colorPalettes.removeAll {
                    it.objectId == palette.objectId
                }
            }
        }
    }

}