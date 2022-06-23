package com.example.colorpalettesapp.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ColorPalette(
    var objectId: String? = null,
    var approved: Boolean = false,
    var colors: String? = null,
    var totalLikes: Int? = null,
): Parcelable
