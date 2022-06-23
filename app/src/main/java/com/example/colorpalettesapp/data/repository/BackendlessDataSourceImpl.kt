package com.example.colorpalettesapp.data.repository

import com.backendless.Backendless
import com.backendless.Persistence
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import com.backendless.persistence.LoadRelationsQueryBuilder
import com.backendless.rt.data.RelationStatus
import com.example.colorpalettesapp.domain.model.ColorPalette
import com.example.colorpalettesapp.domain.model.Users
import com.example.colorpalettesapp.domain.repository.BackendlessDataSource
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BackendlessDataSourceImpl @Inject constructor(
    private val backendless: Persistence
) : BackendlessDataSource {
    override suspend fun getColorPalettes(): List<ColorPalette> {
        val queryBuilder: DataQueryBuilder = DataQueryBuilder
            .create()
            .setProperties("Count(likes) as totalLikes", "colors", "approved", "objectId")
            .setWhereClause("approved = true")
            .setGroupBy("objectId")
        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java)
                .find(queryBuilder, object : AsyncCallback<List<ColorPalette>> {
                    override fun handleResponse(response: List<ColorPalette>) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        continuation.resume(emptyList())
                    }
                })
        }
    }

    override suspend fun getLikeCount(objectId: String): ColorPalette {
        val queryBuilder: DataQueryBuilder = DataQueryBuilder
            .create()
            .setProperties("Count(likes) as totalLikes")

        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java)
                .findById(objectId, queryBuilder, object : AsyncCallback<ColorPalette> {
                    override fun handleResponse(response: ColorPalette) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        continuation.resumeWithException(Exception(fault.message))
                    }
                })
        }
    }

    override suspend fun observeAddRelation(): Flow<RelationStatus?> {
        return callbackFlow {
            val event = backendless.of(ColorPalette::class.java).rt()
            val callback = object : AsyncCallback<RelationStatus> {
                override fun handleResponse(response: RelationStatus?) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addAddRelationListener("likes", callback)
            awaitClose {
                event.removeAddRelationListeners()
            }
        }
    }

    override suspend fun observeDeleteRelation(): Flow<RelationStatus?> {
        return callbackFlow {
            val event = backendless.of(ColorPalette::class.java).rt()
            val callback = object : AsyncCallback<RelationStatus> {
                override fun handleResponse(response: RelationStatus?) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addDeleteRelationListener("likes", callback)
            awaitClose {
                event.removeDeleteRelationListeners()
            }
        }
    }

    override suspend fun observeApproval(): Flow<ColorPalette> {
        return callbackFlow {
            val event = backendless.of(ColorPalette::class.java).rt()
            val callback = object : AsyncCallback<ColorPalette> {
                override fun handleResponse(response: ColorPalette) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addUpdateListener("approved = false or approved = true", callback)
            awaitClose {
                event.removeUpdateListeners()
            }
        }
    }

    override suspend fun observeDeletedPalettes(): Flow<ColorPalette> {
        return callbackFlow {
            val event = backendless.of(ColorPalette::class.java).rt()
            val callback = object : AsyncCallback<ColorPalette> {
                override fun handleResponse(response: ColorPalette) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addDeleteListener(callback)
            awaitClose {
                event.removeDeleteListeners()
            }
        }
    }

    override suspend fun checkSavedPalette(
        paletteObjectId: String,
        userObjectId: String
    ): List<ColorPalette> {
        val query = DataQueryBuilder.create()
            .setWhereClause("Users[saved].objectId = '$userObjectId' and objectId = '$paletteObjectId'")

        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java).find(
                query,
                object : AsyncCallback<List<ColorPalette>> {
                    override fun handleResponse(response: List<ColorPalette>) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resume(emptyList())
                    }
                }
            )
        }
    }

    override suspend fun saveColorPalette(paletteObjectId: String, userObjectId: String): Int {
        return suspendCoroutine { continuation ->
            val user = Users(objectId = userObjectId)

            backendless.of(Users::class.java).addRelation(
                user,
                "saved",
                arrayListOf(ColorPalette(objectId = paletteObjectId)),
                object : AsyncCallback<Int> {
                    override fun handleResponse(response: Int) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                })
        }
    }

    override suspend fun removeColorPalette(paletteObjectId: String, userObjectId: String): Int {
        return suspendCoroutine { continuation ->
            val user = Users(objectId = userObjectId)

            backendless.of(Users::class.java).deleteRelation(
                user,
                "saved",
                arrayListOf(ColorPalette(objectId = paletteObjectId)),
                object : AsyncCallback<Int> {
                    override fun handleResponse(response: Int) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                })
        }
    }

    override suspend fun addLike(paletteObjectId: String, userObjectId: String): Int? {
        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java).addRelation(
                ColorPalette(objectId = paletteObjectId),
                "likes",
                arrayListOf(Users(objectId = userObjectId)),
                object : AsyncCallback<Int> {
                    override fun handleResponse(response: Int?) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                })
        }
    }

    override suspend fun removeLike(paletteObjectId: String, userObjectId: String): Int? {
        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java).deleteRelation(
                ColorPalette(objectId = paletteObjectId),
                "likes",
                arrayListOf(Users(objectId = userObjectId)),
                object : AsyncCallback<Int> {
                    override fun handleResponse(response: Int?) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                })
        }
    }

    override suspend fun getSavedPalettes(userObjectId: String): List<ColorPalette> {
        val relationQuery: LoadRelationsQueryBuilder<ColorPalette> =
            LoadRelationsQueryBuilder.of(ColorPalette::class.java)
        relationQuery.setRelationName("saved")

        return suspendCoroutine { continuation ->
            Backendless.Data.of(Users::class.java).loadRelations(
                userObjectId,
                relationQuery,
                object : AsyncCallback<List<ColorPalette>> {
                    override fun handleResponse(response: List<ColorPalette>) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                }
            )
        }
    }

    override suspend fun observeSavedPalettes(userObjectId: String): Flow<RelationStatus?> {
        return callbackFlow {
            val event = backendless.of(Users::class.java).rt()
            val callback = object : AsyncCallback<RelationStatus> {
                override fun handleResponse(response: RelationStatus?) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addDeleteRelationListener(
                "saved",
                listOf(userObjectId),
                callback
            )
            awaitClose {
                event.removeDeleteRelationListeners()
            }
        }
    }

    override suspend fun getSubmittedPalettes(userObjectId: String): List<ColorPalette> {
        val query = DataQueryBuilder.create()
            .setWhereClause("ownerId = '$userObjectId'")

        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java).find(
                query,
                object : AsyncCallback<List<ColorPalette>> {
                    override fun handleResponse(response: List<ColorPalette>) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                }
            )
        }
    }

    override suspend fun observeSubmittedPalettes(userObjectId: String): Flow<ColorPalette> {
        return callbackFlow {
            val event = backendless.of(ColorPalette::class.java).rt()
            val callback = object : AsyncCallback<ColorPalette> {
                override fun handleResponse(response: ColorPalette) {
                    trySendBlocking(response)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    fault?.message?.let { cancel(message = it) }
                }
            }
            event.addCreateListener("ownerId = '$userObjectId' and approved = false", callback)
            awaitClose {
                event.removeCreateListeners()
            }
        }
    }

    override suspend fun submitColorPalette(colorPalette: ColorPalette): ColorPalette {
        return suspendCoroutine { continuation ->
            backendless.of(ColorPalette::class.java).save(
                colorPalette,
                object : AsyncCallback<ColorPalette> {
                    override fun handleResponse(response: ColorPalette) {
                        continuation.resume(response)
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(Exception(fault?.message))
                    }
                }
            )
        }
    }
}