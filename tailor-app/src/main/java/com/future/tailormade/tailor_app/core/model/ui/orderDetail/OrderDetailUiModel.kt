package com.future.tailormade.tailor_app.core.model.ui.orderDetail

import android.os.Parcelable
import com.future.tailormade.tailor_app.core.model.ui.order.OrderDesignUiModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetailUiModel(

    var orderDate: String,

    var design: OrderDesignUiModel,

    var id: String,

    var measurement: OrderDetailMeasurementUiModel,

    var quantity: String,

    var specialInstructions: String? = null,

    var status: String,

    var totalDiscount: String,

    var totalPrice: String,

    var paymentTotal: String,

    var orderedBy: String
): Parcelable