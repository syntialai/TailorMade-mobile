package com.future.tailormade.tailor_app.core.model.ui.orderDetail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetailMeasurementUiModel(

    var chest: String? = null,

    var hips: String? = null,

    var inseam: String? = null,

    var neckToWaist: String? = null,

    var waist: String? = null
): Parcelable