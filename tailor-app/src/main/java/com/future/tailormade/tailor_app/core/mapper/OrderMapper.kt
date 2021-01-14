package com.future.tailormade.tailor_app.core.mapper

import com.future.tailormade.base.mapper.BaseMapper
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.response.order.OrderDesignResponse
import com.future.tailormade.tailor_app.core.model.response.order.OrderResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailMeasurementResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailResponse
import com.future.tailormade.tailor_app.core.model.ui.order.OrderDesignUiModel
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailMeasurementUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailUiModel
import com.future.tailormade.util.extension.toDateString
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat

object OrderMapper {

  fun mapToOrderUiModel(order: OrderResponse) = OrderUiModel(
      id = order.id,
      orderDate = order.createdAt.toDateString(Constants.DD_MMM_YYYY),
      quantity = order.quantity.toString(),
      totalPrice = order.totalPrice.toIndonesiaCurrencyFormat(),
      totalDiscount = order.totalDiscount.toIndonesiaCurrencyFormat(),
      status = order.status,
      design = mapToOrderDesignUiModel(order.design)
  )

  fun mapToOrderDetailUiModel(orderDetail: OrderDetailResponse) = OrderDetailUiModel(
      id = orderDetail.id,
      orderDate = orderDetail.createdAt.toDateString(Constants.DD_MMMM_YYYY_HH_MM_SS),
      orderedBy = orderDetail.userName,
      quantity = orderDetail.quantity.toString(),
      status = orderDetail.status,
      specialInstructions = orderDetail.specialInstructions,
      design = mapToOrderDesignUiModel(orderDetail.design),
      measurement = mapToOrderDetailMeasurementDetail(orderDetail.measurement),
      totalDiscount = orderDetail.totalDiscount.toIndonesiaCurrencyFormat(),
      totalPrice = orderDetail.totalPrice.toIndonesiaCurrencyFormat(),
      paymentTotal = (orderDetail.totalPrice - orderDetail.totalDiscount).toIndonesiaCurrencyFormat()
  )

  private fun mapToOrderDetailMeasurementDetail(measurement: OrderDetailMeasurementResponse) = OrderDetailMeasurementUiModel(
      chest = measurement.chest.toString(),
      waist = measurement.waist.toString(),
      hips = measurement.hips.toString(),
      neckToWaist = measurement.neckToWaist.toString(),
      inseam = measurement.inseam.toString()
  )

  private fun mapToOrderDesignUiModel(design: OrderDesignResponse) = OrderDesignUiModel(
      id = design.id,
      title = design.title,
      image = design.image,
      size = design.size,
      color = design.color,
      price = design.price.toIndonesiaCurrencyFormat(),
      discount = BaseMapper.setDiscount(design.price, design.discount)
  )
}