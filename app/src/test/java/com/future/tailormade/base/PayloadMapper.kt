package com.future.tailormade.base

import com.future.tailormade.base.test.BaseTest
import com.future.tailormade.config.Constants
import com.future.tailormade.core.mapper.CartMapper
import com.future.tailormade.core.mapper.OrderMapper
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.request.checkout.CheckoutMeasurementRequest
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.response.cart.CartDesignResponse
import com.future.tailormade.core.model.response.cart.CartEditQuantityResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.model.response.checkout.CheckoutDesignResponse
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.model.response.history.OrderDesignResponse
import com.future.tailormade.core.model.response.history.OrderDetailMeasurementResponse
import com.future.tailormade.core.model.response.history.OrderDetailResponse
import com.future.tailormade.core.model.response.history.OrderResponse
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel
import com.future.tailormade_design_detail.core.model.response.AddToCartResponse
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
   * Cart Payloads
   */
  fun getCartDesignResponse() = CartDesignResponse(color = COLOR,
      discount = BaseTest.DESIGN_DISCOUNT, id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
      price = BaseTest.DESIGN_PRICE, size = SIZE, title = BaseTest.DESIGN_TITLE)

  fun getCartResponse() = CartResponse(createdAt = BaseTest.ORDER_DATE,
      design = getCartDesignResponse(), id = BaseTest.ORDER_ID, quantity = BaseTest.DESIGN_QUANTITY,
      tailorId = BaseTest.TAILOR_ID, updatedAt = BaseTest.ORDER_DATE, userId = BaseTest.USER_ID,
      tailorName = BaseTest.TAILOR_NAME, userName = BaseTest.USER_NAME)

  fun getCartResponses() = listOf(getCartResponse())

  fun getCartUiModel() = CartMapper.mapToCartUiModel(getCartResponse())

  fun getCartUiModels() = CartMapper.mapToCartUiModel(getCartResponses())

  fun getDeleteCartResponse() = AddToCartResponse(BaseTest.CART_ID)

  fun getEditCartQuantityRequest() = CartEditQuantityRequest(BaseTest.DESIGN_QUANTITY)

  fun getEditCartQuantityResponse() = CartEditQuantityResponse(id = BaseTest.CART_ID,
      quantity = BaseTest.DESIGN_QUANTITY)

  /**
   * Checkout Payloads
   */
  fun getCheckoutMeasurementRequest() = CheckoutMeasurementRequest(chest = 12f, hips = 13f,
      inseam = 14f, neckToWaist = 15f, waist = 16f)

  fun getCheckoutRequest() = CheckoutRequest(measurements = getCheckoutMeasurementRequest(),
      specialInstructions = SPECIAL_INSTRUCTIONS)

  fun getCheckoutDesignResponse() = CheckoutDesignResponse(color = COLOR,
      discount = BaseTest.DESIGN_DISCOUNT, id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
      price = BaseTest.DESIGN_PRICE, size = SIZE, title = BaseTest.DESIGN_TITLE)

  fun getCheckoutResponse() = CheckoutResponse(design = getCheckoutDesignResponse(),
      id = BaseTest.ORDER_ID, quantity = BaseTest.DESIGN_QUANTITY,
      tailorId = BaseTest.TAILOR_ID, totalDiscount = BaseTest.DESIGN_DISCOUNT,
      totalPrice = BaseTest.DESIGN_PRICE,
      userId = BaseTest.USER_ID)

  /**
   * Dashboard Payloads
   */
  fun getDashboardTailorsResponse() = listOf(
      DashboardTailorResponse(id = BaseTest.TAILOR_ID, name = BaseTest.TAILOR_NAME))

  fun getDashboardTailorsUiModel() = arrayListOf(
      DashboardTailorUiModel(id = BaseTest.TAILOR_ID, name = BaseTest.TAILOR_NAME))

  /**
   * Order Payloads
   */
  fun getOrdersResponse() = listOf(
      OrderResponse(createdAt = BaseTest.ORDER_DATE, design = getOrderDesignResponse(),
          id = BaseTest.ORDER_ID, quantity = BaseTest.DESIGN_QUANTITY, status = STATUS,
          tailorId = BaseTest.TAILOR_ID, totalDiscount = BaseTest.DESIGN_DISCOUNT,
          totalPrice = BaseTest.DESIGN_PRICE, updatedAt = BaseTest.ORDER_DATE,
          userId = BaseTest.USER_ID))

  fun getOrdersUiModel() = arrayListOf(OrderMapper.mapToHistoryUiModel(getOrdersResponse()[0]))

  fun getOrderDetailResponse() = OrderDetailResponse(createdAt = BaseTest.ORDER_DATE,
      design = getOrderDesignResponse(), id = BaseTest.ORDER_ID,
      quantity = BaseTest.DESIGN_QUANTITY, status = STATUS, tailorId = BaseTest.TAILOR_ID,
      totalDiscount = BaseTest.DESIGN_DISCOUNT, totalPrice = BaseTest.DESIGN_PRICE,
      updatedAt = BaseTest.ORDER_DATE, userId = BaseTest.USER_ID,
      measurement = getOrderDetailMeasurementResponse(), tailorName = BaseTest.TAILOR_NAME,
      userName = BaseTest.USER_NAME, specialInstructions = SPECIAL_INSTRUCTIONS)

  fun getOrderDetailUiModel() = OrderMapper.mapToHistoryDetailUiModel(getOrderDetailResponse())

  fun getOrderDesignResponse() = OrderDesignResponse(color = COLOR,
      discount = BaseTest.DESIGN_DISCOUNT, id = BaseTest.DESIGN_ID, image = BaseTest.DESIGN_IMAGE,
      price = BaseTest.DESIGN_PRICE, size = SIZE, tailorId = BaseTest.TAILOR_ID,
      tailorName = BaseTest.TAILOR_NAME, title = BaseTest.DESIGN_TITLE)

  fun getOrderDetailMeasurementResponse() = OrderDetailMeasurementResponse(chest = 12f, hips = 13f,
      inseam = 14f, neckToWaist = 15f, waist = 16f)
}

