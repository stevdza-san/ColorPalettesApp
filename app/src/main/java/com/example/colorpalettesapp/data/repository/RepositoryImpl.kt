package com.example.colorpalettesapp.data.repository

import com.backendless.rt.data.RelationStatus
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.domain.repository.BackendlessDataSource
import com.example.colorpalettesapp.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val backendlessDataSource: BackendlessDataSource
) : Repository {
    override suspend fun getColorPalettes(): List<ColorPalette> {
        return backendlessDataSource.getColorPalettes()
    }

    override suspend fun getLikeCount(objectId: String): ColorPalette {
        return backendlessDataSource.getLikeCount(objectId = objectId)
    }

    override suspend fun observeAddRelation(): Flow<RelationStatus?> {
        return backendlessDataSource.observeAddRelation()
    }

    override suspend fun observeDeleteRelation(): Flow<RelationStatus?> {
        return backendlessDataSource.observeDeleteRelation()

    }

    override suspend fun observeApproval(): Flow<ColorPalette> {
        return backendlessDataSource.observeApproval()
    }

    override suspend fun observeDeletedPalettes(): Flow<ColorPalette> {
        return backendlessDataSource.observeDeletedPalettes()
    }

    override suspend fun checkSavedPalette(
        paletteObjectId: String,
        userObjectId: String
    ): List<ColorPalette> {
        return backendlessDataSource.checkSavedPalette(
            paletteObjectId = paletteObjectId,
            userObjectId = userObjectId
        )
    }

    override suspend fun saveColorPalette(paletteObjectId: String, userObjectId: String): Int {
        return backendlessDataSource.saveColorPalette(
            paletteObjectId = paletteObjectId,
            userObjectId = userObjectId
        )
    }

    override suspend fun removeColorPalette(paletteObjectId: String, userObjectId: String): Int {
        return backendlessDataSource.removeColorPalette(
            paletteObjectId = paletteObjectId,
            userObjectId = userObjectId
        )
    }

    override suspend fun addLike(paletteObjectId: String, userObjectId: String): Int? {
        return backendlessDataSource.addLike(
            paletteObjectId = paletteObjectId,
            userObjectId = userObjectId
        )
    }

    override suspend fun removeLike(paletteObjectId: String, userObjectId: String): Int? {
        return backendlessDataSource.removeLike(
            paletteObjectId = paletteObjectId,
            userObjectId = userObjectId
        )
    }

    override suspend fun getSavedPalettes(userObjectId: String): List<ColorPalette> {
        return backendlessDataSource.getSavedPalettes(userObjectId = userObjectId)
    }

    override suspend fun observeSavedPalettes(): Flow<RelationStatus?> {
        return backendlessDataSource.observeSavedPalettes()
    }

    override suspend fun getSubmittedPalettes(userObjectId: String): List<ColorPalette> {
        return backendlessDataSource.getSubmittedPalettes(userObjectId = userObjectId)
    }

    override suspend fun observeSubmittedPalettes(userObjectId: String): Flow<ColorPalette> {
        return backendlessDataSource.observeSubmittedPalettes(userObjectId = userObjectId)
    }

    override suspend fun submitColorPalette(colorPalette: ColorPalette): ColorPalette {
        return backendlessDataSource.submitColorPalette(colorPalette = colorPalette)
    }
}