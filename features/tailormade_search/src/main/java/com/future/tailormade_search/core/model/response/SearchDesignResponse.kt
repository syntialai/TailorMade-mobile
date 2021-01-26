package com.future.tailormade_search.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchDesignResponse(

    val id: String,

    val title: String,

    val price: Double,

    val discount: Double,

    val imagePath: String): Parcelable