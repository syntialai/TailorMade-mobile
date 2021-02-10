package com.future.tailormade.tailor_app.core.model.ui.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDesignUiModel(

    var color: String,

    var discount: String? = null,

    var id: String,

    var image: String,

    var price: String,

    var size: String,

    var title: String) : Parcelable