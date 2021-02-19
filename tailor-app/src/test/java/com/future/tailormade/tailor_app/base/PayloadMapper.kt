package com.future.tailormade.tailor_app.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.mapper.OrderMapper
import com.future.tailormade.tailor_app.core.model.response.order.OrderDesignResponse
import com.future.tailormade.tailor_app.core.model.response.order.OrderResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailMeasurementResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailResponse
import com.future.tailormade.tailor_app.core.model.ui.dashboard.DashboardDesignUiModel
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
object PayloadMapper {

  const val COLOR = "COLOR"
  const val SIZE = "SIZE"
  const val STATUS = Constants.STATUS_ACCEPTED
  const val SPECIAL_INSTRUCTIONS = "SPECIAL INSTRUCTIONS"

  /**
   * Dashboard Payloads
   */
  fun getDashboardDesignsResponse() = listOf(
      ProfileDesignResponse(id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
          title = BaseTest.DESIGN_TITLE, price = BaseTest.DESIGN_PRICE,
          discount = BaseTest.DESIGN_DISCOUNT, active = true))

  fun getDashboardDesignsUiModel() = arrayListOf(
      DashboardDesignUiModel(id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
          title = BaseTest.DESIGN_TITLE, price = BaseTest.DESIGN_PRICE_UI_MODEL,
          discount = null, active = true))

  /**
   * Order Payloads
   */
  fun getOrdersResponse() = listOf(
      OrderResponse(createdAt = BaseTest.ORDER_DATE, design = getOrderDesignResponse(),
          id = BaseTest.ORDER_ID, quantity = BaseTest.DESIGN_QUANTITY, status = STATUS,
          tailorId = BaseTest.TAILOR_ID, totalDiscount = BaseTest.DESIGN_DISCOUNT,
          totalPrice = BaseTest.DESIGN_PRICE, updatedAt = BaseTest.ORDER_DATE,
          userId = BaseTest.USER_ID))

  fun getOrdersUiModel() = arrayListOf(OrderMapper.mapToOrderUiModel(getOrdersResponse()[0]))

  fun getOrderDetailResponse() = OrderDetailResponse(createdAt = BaseTest.ORDER_DATE,
      design = getOrderDesignResponse(), id = BaseTest.ORDER_ID,
      quantity = BaseTest.DESIGN_QUANTITY, status = STATUS, tailorId = BaseTest.TAILOR_ID,
      totalDiscount = BaseTest.DESIGN_DISCOUNT, totalPrice = BaseTest.DESIGN_PRICE,
      updatedAt = BaseTest.ORDER_DATE, userId = BaseTest.USER_ID,
      measurement = getOrderDetailMeasurementResponse(), tailorName = BaseTest.TAILOR_NAME,
      userName = BaseTest.USER_NAME, specialInstructions = SPECIAL_INSTRUCTIONS)

  fun getOrderDetailUiModel() = OrderMapper.mapToOrderDetailUiModel(getOrderDetailResponse())

  fun getOrderDesignResponse() = OrderDesignResponse(color = COLOR,
      discount = BaseTest.DESIGN_DISCOUNT, id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
      price = BaseTest.DESIGN_PRICE, size = SIZE, tailorId = BaseTest.TAILOR_ID,
      tailorName = BaseTest.TAILOR_NAME, title = BaseTest.DESIGN_TITLE)

  fun getOrderDetailMeasurementResponse() = OrderDetailMeasurementResponse(chest = 12f, hips = 13f,
      inseam = 14f, neckToWaist = 15f, waist = 16f)
}

