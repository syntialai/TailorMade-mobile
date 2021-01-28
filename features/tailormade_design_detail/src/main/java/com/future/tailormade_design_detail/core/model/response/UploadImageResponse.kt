package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UploadImageResponse(

    val imagePath: String
): Parcelable
