package com.example.colorpalettesapp.domain.model

data class Users(
    var id: String? = null,
    var ownerId: String? = null,
    var objectId: String? = null,
    var email: String? = null,
    var saved: List<ColorPalette>? = null
)
