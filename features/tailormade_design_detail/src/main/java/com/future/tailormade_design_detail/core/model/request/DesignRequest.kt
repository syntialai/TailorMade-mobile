package com.future.tailormade_design_detail.core.model.request

data class DesignRequest(

    val title: String,

    val image: String,

    val price: Double,

    val discount: Double,

    val description: String = "",

    val size: List<DesignSizeRequest>,

    val color: List<DesignColorRequest>,

    val category: List<String>? = null
)