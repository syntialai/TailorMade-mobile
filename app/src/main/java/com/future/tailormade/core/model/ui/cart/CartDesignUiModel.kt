package com.future.tailormade.core.model.ui.cart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartDesignUiModel(

    var id: String,

    var color: String,

    var discount: String? = null,

    var image: String,

    var price: String,

    var size: String,

    var title: String): Parcelable