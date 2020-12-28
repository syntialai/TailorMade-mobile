package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorResponse(

    val id: String,

    val color: String): Parcelable
