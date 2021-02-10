package com.future.tailormade_profile.core.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(

    val lat: Float,

    val lon: Float,

    val address: String,

    val district: String,

    val city: String,

    val province: String,

    val country: String,

    val postCode: String) : Parcelable