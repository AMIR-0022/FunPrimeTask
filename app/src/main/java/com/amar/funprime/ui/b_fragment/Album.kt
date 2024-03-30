package com.amar.funprime.ui.b_fragment


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Album(
    val id: Int,
    val albumId: Int,
    val title: String,
    val url: String,
    val thumbnailUrl: String
) : Parcelable
