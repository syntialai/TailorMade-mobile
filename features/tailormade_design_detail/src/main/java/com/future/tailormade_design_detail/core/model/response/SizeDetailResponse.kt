package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeDetailResponse(

    val chest: Float? = null,

    val waist: Float? = null,

    val hips: Float? = null,

    val neckToWaist: Float? = null,

    val inseam: Float? = null,
): Parcelable
