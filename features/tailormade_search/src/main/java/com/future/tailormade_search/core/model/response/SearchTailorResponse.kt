package com.future.tailormade_search.core.model.response

import android.os.Parcelable
import com.future.tailormade_search.core.model.entity.Location
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchTailorResponse(

    val id: String,

    val name: String,

    val imagePath: String? = null,

    val location: Location? = null): Parcelable