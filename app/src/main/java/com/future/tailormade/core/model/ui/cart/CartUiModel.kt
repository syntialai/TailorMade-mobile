package com.future.tailormade.core.model.ui.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartUiModel(

    var id: String,

    var design: CartDesignUiModel,

    var quantity: Int = 1,

    var totalPrice: String? = null,

    var totalDiscount: String = "-",

    var totalPayment: String? = null
): Parcelable