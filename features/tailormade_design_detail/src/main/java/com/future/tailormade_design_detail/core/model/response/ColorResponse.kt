package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorResponse(

    val name: String,

    val color: String): Parcelable
