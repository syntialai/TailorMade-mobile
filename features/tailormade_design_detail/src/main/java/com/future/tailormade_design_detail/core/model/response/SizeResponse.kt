package com.future.tailormade_design_detail.core.model.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SizeResponse(

    val name: String,

    @Expose
    val detail: SizeDetailResponse? = null) : Parcelable
