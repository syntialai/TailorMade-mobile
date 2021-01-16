package com.future.tailormade.core.mapper

import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.model.ui.history.OrderDesignUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailMeasurementUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailUiModel
import com.future.tailormade.core.model.ui.history.OrderUiModel
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat

object OrderMapper {

  fun mapToHistoryUiModel(order: OrderResponse) = OrderUiModel(
      id = order.id,
      orderDate = order.createdAt.toDateString(Constants.DD_MMM_YYYY),
      quantity = order.quantity.toString(),
      status = order.status,
      design = mapToHistoryDetailDesign(order.design),
      totalDiscount = order.totalDiscount.toIndonesiaCurrencyFormat(),
      totalPrice = order.totalPrice.toIndonesiaCurrencyFormat()
  )

  fun mapToHistoryDetailUiModel(orderDetail: OrderDetailResponse) = OrderDetailUiModel(
      id = orderDetail.id,
      orderDate = orderDetail.createdAt.toDateString(Constants.DD_MMMM_YYYY_HH_MM_SS),
      orderedBy = orderDetail.userName,
      quantity = orderDetail.quantity.toString(),
      status = orderDetail.status,
      specialInstructions = orderDetail.specialInstructions,
      design = mapToHistoryDetailDesign(orderDetail.design),
      measurement = mapToHistoryDetailMeasurementDetail(orderDetail.measurement),
      totalDiscount = orderDetail.totalDiscount.toIndonesiaCurrencyFormat(),
      totalPrice = orderDetail.totalPrice.toIndonesiaCurrencyFormat(),
      paymentTotal = (orderDetail.totalPrice - orderDetail.totalDiscount).toIndonesiaCurrencyFormat()
  )

  private fun mapToHistoryDetailMeasurementDetail(measurement: OrderDetailMeasurementResponse) = OrderDetailMeasurementUiModel(
      chest = measurement.chest.toString() + "cm",
      waist = measurement.waist.toString() + "cm",
      hips = measurement.hips.toString() + "cm",
      neckToWaist = measurement.neckToWaist.toString() + "cm",
      inseam = measurement.inseam.toString() + "cm"
  )

  private fun mapToHistoryDetailDesign(design: OrderDesignResponse) = OrderDesignUiModel(
      id = design.id,
      image = design.image,
      title = design.title,
      size = design.size,
      color = design.color,
      price = design.price.toIndonesiaCurrencyFormat(),
      discount = getDiscountPrice(design.price, design.discount)
  )

  private fun getDiscountPrice(price: Double, discount: Double) = if (discount > 0.0) {
    (price - discount).toIndonesiaCurrencyFormat()
  } else {
    null
  }
}