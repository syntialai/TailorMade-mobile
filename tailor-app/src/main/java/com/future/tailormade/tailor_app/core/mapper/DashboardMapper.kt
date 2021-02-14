package com.future.tailormade.tailor_app.core.mapper

import com.future.tailormade.tailor_app.core.model.ui.dashboard.DashboardDesignUiModel
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse

object DashboardMapper {

  fun mapToDashboardDesignUiModel(dashboardDesignResponse: ProfileDesignResponse): DashboardDesignUiModel {
    return DashboardDesignUiModel(id = dashboardDesignResponse.id,
        image = dashboardDesignResponse.image, title = dashboardDesignResponse.title,
        active = dashboardDesignResponse.active,
        price = dashboardDesignResponse.price.toIndonesiaCurrencyFormat(),
        discount = setDiscount(dashboardDesignResponse.price, dashboardDesignResponse.discount))
  }

  private fun setDiscount(price: Double, discount: Double) = if (discount > 0) {
    (price - discount).toIndonesiaCurrencyFormat()
  } else {
    null
  }
}