package com.future.tailormade_design_detail.core.model.ui

import android.os.Parcelable
import com.future.tailormade_design_detail.core.model.response.ColorResponse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DesignDetailUiModel(

		var id: String,

		var title: String,

		var image: String,

		var price: String,

		var discount: String? = null,

		var tailorId: String,

		var tailorName: String,

		var description: String,

		var size: MutableList<SizeUiModel>,

		var color: List<ColorResponse>,

		var category: List<String>): Parcelable