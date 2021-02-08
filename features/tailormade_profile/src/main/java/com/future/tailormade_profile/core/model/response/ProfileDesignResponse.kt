package com.future.tailormade_profile.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProfileDesignResponse(

    val id: String,

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double,

    val active: Boolean) : Parcelable