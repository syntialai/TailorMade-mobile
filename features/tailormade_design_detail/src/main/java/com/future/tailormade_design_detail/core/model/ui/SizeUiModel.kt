package com.future.tailormade_design_detail.core.model.ui

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeUiModel(

		var name: String? = null,

		var detail: SizeDetailUiModel? = null): Parcelable
