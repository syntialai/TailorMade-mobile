package com.future.tailormade.tailor_app.core.mock

import com.future.tailormade.tailor_app.core.mapper.DashboardMapper
import com.future.tailormade.tailor_app.core.mapper.OrderMapper
import com.future.tailormade.tailor_app.core.model.response.order.OrderDesignResponse
import com.future.tailormade.tailor_app.core.model.response.order.OrderResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailMeasurementResponse
import com.future.tailormade.tailor_app.core.model.response.orderDetail.OrderDetailResponse
import com.future.tailormade.tailor_app.core.model.ui.order.OrderUiModel
import com.future.tailormade.tailor_app.core.model.ui.orderDetail.OrderDetailUiModel
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse

object DataMock {

  private const val USER_ID = "USER_ID"
  private const val USER_NAME = "USER_NAME"
  private const val TAILOR_ID = "TAILOR_ID"
  private const val TAILOR_NAME = "TAILOR_NAME"
  private const val ORDER_ID = "ORDER_ID"
  private const val DESIGN_ID = "DESIGN_ID"
  private const val DISCOUNT = 0.0
  private const val PRICE = 50000.0

  private val profileDesignMock = ProfileDesignResponse(id = DESIGN_ID,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      title = "Design 1", price = PRICE, discount = DISCOUNT, active = true)

  private val orderDesignMock = OrderDesignResponse(id = DESIGN_ID,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      color = "Blue", discount = DISCOUNT, price = PRICE, size = "S", title = "Design 1",
      tailorId = TAILOR_ID, tailorName = TAILOR_NAME)
  private val orderMeasurementMock = OrderDetailMeasurementResponse(chest = 98.4f, hips = 40f,
      inseam = 50.0f, neckToWaist = 48f, waist = 70.5f)

  fun getProfileDesignsMock() = arrayListOf(
      DashboardMapper.mapToDashboardDesignUiModel(profileDesignMock))

  fun getOrdersMock(): ArrayList<OrderUiModel> {
    val orderResponse = OrderResponse(id = ORDER_ID, createdAt = 1609644391, updatedAt = 160964500,
        quantity = 1, status = "Accepted", tailorId = TAILOR_ID, userId = USER_ID,
        totalPrice = PRICE, totalDiscount = DISCOUNT, design = orderDesignMock)
    return arrayListOf(OrderMapper.mapToOrderUiModel(orderResponse))
  }

  fun getOrderDetailMock(): OrderDetailUiModel {
    val orderDetailResponse = OrderDetailResponse(1610771710, orderDesignMock, ORDER_ID,
        orderMeasurementMock, 2, "What ever", "Accepted", TAILOR_ID, TAILOR_NAME, DISCOUNT, PRICE,
        1610772267, USER_ID, USER_NAME)
    return OrderMapper.mapToOrderDetailUiModel(orderDetailResponse)
  }
}