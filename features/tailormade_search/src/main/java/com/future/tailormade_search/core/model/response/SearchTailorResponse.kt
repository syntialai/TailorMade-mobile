package com.future.tailormade_search.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchTailorResponse(

    val id: String,

    val name: String,

    val imagePath: String? = null,

    val location: String? = null): Parcelable