package com.future.tailormade.tailor_app.core.model.ui.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize data class OrderUiModel(

    var id: String,

    var orderDate: String,

    var design: OrderDesignUiModel,

    var quantity: String,

    var totalDiscount: String,

    var totalPrice: String,

    var status: String) : Parcelable