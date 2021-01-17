package com.future.tailormade.tailor_app.core.model.ui.dashboard

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DashboardDesignUiModel(

    var id: String,

    var title: String,

    var image: String,

    var price: String,

    var active: Boolean) : Parcelable