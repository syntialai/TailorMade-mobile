package com.future.tailormade_profile.core.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Occupation (

    val company: String? = null,

    val city: String? = null,

    val job: String? = null): Parcelable