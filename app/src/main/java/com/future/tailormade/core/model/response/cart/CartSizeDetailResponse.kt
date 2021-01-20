package com.future.tailormade.core.model.response.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartSizeDetailResponse(

    val chest: Float? = null,

    val waist: Float? = null,

    val hips: Float? = null,

    val neckToWaist: Float? = null,

    val inseam: Float? = null): Parcelable