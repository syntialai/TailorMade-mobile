package com.future.tailormade_profile.core.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Education (

    val school: String? = null,

    val major: String? = null,

    val city: String? = null): Parcelable