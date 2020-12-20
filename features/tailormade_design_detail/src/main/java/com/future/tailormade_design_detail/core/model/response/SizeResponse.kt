package com.future.tailormade_design_detail.core.model.response

import com.google.gson.annotations.Expose

data class SizeResponse(

    val id: String,

    @Expose
    val detail: SizeDetailResponse? = null
)
