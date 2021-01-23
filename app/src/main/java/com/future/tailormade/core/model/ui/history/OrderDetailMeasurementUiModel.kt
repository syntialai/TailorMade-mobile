package com.future.tailormade.core.model.ui.history

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