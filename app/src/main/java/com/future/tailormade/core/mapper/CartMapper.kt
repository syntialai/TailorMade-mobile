package com.future.tailormade.core.mapper

import com.future.tailormade.core.model.response.cart.CartDesignResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat

object CartMapper {

  fun mapToCartUiModel(carts: List<CartResponse>): ArrayList<CartUiModel> {
    val cartUiModels = arrayListOf<CartUiModel>()
    carts.forEach { cart ->
      val cartUiModel = mapToCartUiModel(cart)
      cartUiModels.add(cartUiModel)
    }
    return cartUiModels
  }

  fun mapToCartUiModel(cartResponse: CartResponse) = CartUiModel(
      id = cartResponse.id,
      design = mapToCartDesignUiModel(cartResponse.design),
      quantity = cartResponse.quantity,
      totalPrice = getTotalText(cartResponse.design.price, cartResponse.quantity),
      totalDiscount = getTotalText(cartResponse.design.discount, cartResponse.quantity),
      totalPayment = getTotalText(
          cartResponse.design.price - cartResponse.design.discount, cartResponse.quantity)
  )

  private fun mapToCartDesignUiModel(design: CartDesignResponse) = CartDesignUiModel(
      id = design.id,
      color = design.color,
      discount = setDiscount(design.price, design.discount),
      image = design.image,
      price = design.price.toIndonesiaCurrencyFormat(),
      size = design.size,
      title = design.title
  )

  private fun getTotalText(
      price: Double, quantity: Int) = (price * quantity).toIndonesiaCurrencyFormat()

  private fun setDiscount(price: Double, discount: Double) = if (discount == 0.0) {
    null
  } else {
    (price - discount).toIndonesiaCurrencyFormat()
  }
}