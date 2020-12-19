package com.future.tailormade.core.model.response.history

data class OrderDetailResponse(

    val createdAt: Long,

    val design: OrderDesignResponse,

    val id: String,

    val measurement: OrderDetailMeasurementResponse,

    val quantity: Int,

    val specialInstructions: String? = null,

    val status: String,

    val tailorId: String,

    val tailorName: String,

    val totalDiscount: Double,

    val totalPrice: Double,

    val updatedAt: Long,

    val userId: String,

    val userName: String
)