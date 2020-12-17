package com.future.tailormade.core.model.response.history

import com.google.gson.annotations.Expose

data class OrderDesignResponse(

    val color: String,

    val discount: Double,

    val id: String,

    val image: String,

    val price: Double,

    val size: String,

		@Expose
		val tailorId: String? = null,

		@Expose
		val tailorName: String? = null,

		val title: String
)