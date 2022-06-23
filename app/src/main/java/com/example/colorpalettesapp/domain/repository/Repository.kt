package com.example.colorpalettesapp.domain.repository

import com.backendless.rt.data.RelationStatus
import com.example.colorpalettesapp.domain.model.ColorPalette
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getColorPalettes(): List<ColorPalette>
    suspend fun getLikeCount(objectId: String): ColorPalette
    suspend fun observeAddRelation(): Flow<RelationStatus?>
    suspend fun observeDeleteRelation(): Flow<RelationStatus?>
    suspend fun observeApproval(): Flow<ColorPalette>
    suspend fun observeDeletedPalettes(): Flow<ColorPalette>
    suspend fun checkSavedPalette(paletteObjectId: String, userObjectId: String): List<ColorPalette>
    suspend fun saveColorPalette(paletteObjectId: String, userObjectId: String): Int
    suspend fun removeColorPalette(paletteObjectId: String, userObjectId: String): Int
    suspend fun addLike(paletteObjectId: String, userObjectId: String): Int?
    suspend fun removeLike(paletteObjectId: String, userObjectId: String): Int?
    suspend fun getSavedPalettes(userObjectId: String): List<ColorPalette>
    suspend fun observeSavedPalettes(userObjectId: String): Flow<RelationStatus?>
    suspend fun getSubmittedPalettes(userObjectId: String): List<ColorPalette>
    suspend fun observeSubmittedPalettes(userObjectId: String): Flow<ColorPalette>
    suspend fun submitColorPalette(colorPalette: ColorPalette): ColorPalette
}