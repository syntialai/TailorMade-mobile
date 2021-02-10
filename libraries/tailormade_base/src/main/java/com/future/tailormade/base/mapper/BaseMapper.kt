package com.future.tailormade.base.mapper

import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat

object BaseMapper {

  fun setDiscount(price: Double, discount: Double, default: String? = null) = if (discount > 0.0) {
    (price - discount).toIndonesiaCurrencyFormat()
  } else {
    default
  }
}