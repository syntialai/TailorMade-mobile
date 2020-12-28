package com.future.tailormade_design_detail.core.model.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeDetailUiModel(

		var chest: String = "-",

		var waist: String = "-",

		var hips: String = "-",

		var neckToWaist: String = "-",

		var inseam: String = "-"): Parcelable
