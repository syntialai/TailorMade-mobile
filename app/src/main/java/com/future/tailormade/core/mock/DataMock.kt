package com.future.tailormade.core.mock

import com.future.tailormade.core.mapper.CartMapper
import com.future.tailormade.core.mapper.DashboardMapper
import com.future.tailormade.core.mapper.OrderMapper
import com.future.tailormade.core.model.response.cart.CartDesignResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.model.response.cart.CartSizeDetailResponse
import com.future.tailormade.core.model.response.checkout.CheckoutDesignResponse
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import com.future.tailormade.core.model.response.dashboard.DashboardDesignResponse
import com.future.tailormade.core.model.response.dashboard.DashboardLocationResponse
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.model.ui.history.OrderDetailUiModel
import com.future.tailormade.core.model.ui.history.OrderUiModel

object DataMock {

  private const val USER_ID = "USER_ID"
  private const val USER_NAME = "USER_NAME"
  private const val TAILOR_ID = "TAILOR_ID"
  private const val TAILOR_NAME = "TAILOR_NAME"
  private const val ORDER_ID = "ORDER_ID"
  private const val DESIGN_ID = "DESIGN_ID"
  private const val CART_ID = "CART_ID"
  private const val DISCOUNT = 0.0
  private const val PRICE = 50000.0

  private val dashboardLocationMock = DashboardLocationResponse(city = "Medan",
      country = "Indonesia")
  private val dashboardDesignMock = DashboardDesignResponse(id = "DESIGN_1",
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png")
  private val cartSizeDetailMock = CartSizeDetailResponse(chest = 100f, waist = 150f, hips = 20f,
      neckToWaist = 50f, inseam = 75f)
  private val cartDesignMock = CartDesignResponse(id = DESIGN_ID,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      color = "Blue", discount = DISCOUNT, price = PRICE, size = "S", title = "Design 1",
      sizeDetail = cartSizeDetailMock)
  private val checkoutDesignMock = CheckoutDesignResponse(id = DESIGN_ID,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      color = "Blue", discount = DISCOUNT, price = PRICE, size = "S", title = "Design 1")
  private val orderDesignMock = OrderDesignResponse(id = DESIGN_ID,
      image = "https://www.talkwalker.com/images/2020/blog-headers/image-analysis.png",
      color = "Blue", discount = DISCOUNT, price = PRICE, size = "S", title = "Design 1",
      tailorId = TAILOR_ID, tailorName = TAILOR_NAME)
  private val orderMeasurementMock = OrderDetailMeasurementResponse(chest = 98.4f, hips = 40f,
      inseam = 50.0f, neckToWaist = 48f, waist = 70.5f)

  fun getDashboardTailorsMock() = arrayListOf(DashboardMapper.mapToDashboardTailorUiModel(
      DashboardTailorResponse(id = TAILOR_ID, name = TAILOR_NAME,
          image = "https://miro.medium.com/max/1200/1*mk1-6aYaf_Bes1E3Imhc0A.jpeg",
          location = dashboardLocationMock, designs = listOf(dashboardDesignMock))))

  fun getCartsMock(): ArrayList<CartUiModel> {
    return arrayListOf(getCartByIdMock())
  }

  fun getCartByIdMock(): CartUiModel {
    val cartUiModel = CartResponse(createdAt = 1609644391, id = CART_ID, quantity = 1,
        tailorId = TAILOR_ID, updatedAt = 1609644400, userId = USER_ID,
        tailorName = TAILOR_NAME, userName = USER_NAME, design = cartDesignMock)
    return CartMapper.mapToCartUiModel(cartUiModel)
  }

  fun getCheckoutMock() = CheckoutResponse(id = ORDER_ID, quantity = 1, tailorId = TAILOR_ID,
      userId = USER_ID, totalPrice = PRICE, totalDiscount = DISCOUNT, design = checkoutDesignMock)

  fun getOrdersMock(): ArrayList<OrderUiModel> {
    val orderResponse = OrderResponse(id = ORDER_ID, createdAt = 1609644391, updatedAt = 160964500,
        quantity = 1, status = "Accepted", tailorId = TAILOR_ID, userId = USER_ID,
        totalPrice = PRICE, totalDiscount = DISCOUNT, design = orderDesignMock)
    return arrayListOf(OrderMapper.mapToHistoryUiModel(orderResponse))
  }

  fun getOrderDetailMock(): OrderDetailUiModel {
    val orderDetailResponse = OrderDetailResponse(1610771710, orderDesignMock, ORDER_ID,
        orderMeasurementMock, 2, "What ever", "Accepted", TAILOR_ID, TAILOR_NAME, DISCOUNT, PRICE,
        1610772267, USER_ID, USER_NAME)
    return OrderMapper.mapToHistoryDetailUiModel(orderDetailResponse)
  }
}