package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeDetailResponse(

    val chest: Double? = null,

    val waist: Double? = null,

    val hips: Double? = null,

    val neckToWaist: Double? = null,

    val inseam: Double? = null,
): Parcelable
