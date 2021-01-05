package com.future.tailormade.tailor_app.core.mapper

import com.future.tailormade.tailor_app.core.model.ui.DashboardDesignUiModel
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
import com.future.tailormade_profile.core.model.response.ProfileDesignResponse

object DashboardMapper {

  fun mapToDashboardDesignUiModel(
      dashboardDesignResponse: ProfileDesignResponse): DashboardDesignUiModel {
    val price = (dashboardDesignResponse.price -
                 dashboardDesignResponse.discount).toIndonesiaCurrencyFormat()
    return DashboardDesignUiModel(
        id = dashboardDesignResponse.id,
        image = dashboardDesignResponse.image,
        title = dashboardDesignResponse.title,
        active = dashboardDesignResponse.active,
        price = price
    )
  }
}