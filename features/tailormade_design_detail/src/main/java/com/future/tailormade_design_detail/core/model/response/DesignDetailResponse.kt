package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DesignDetailResponse(

    val id: String,

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double = 0.0,

    val tailorId: String,

    val tailorName: String,

    val description: String,

    val size: List<SizeResponse>,

    val color: List<ColorResponse>,

    val category: List<String>,

    val active: Boolean
): Parcelable
