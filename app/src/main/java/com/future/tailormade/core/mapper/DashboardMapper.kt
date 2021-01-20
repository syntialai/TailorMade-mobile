package com.future.tailormade.core.mapper

import com.future.tailormade.core.model.response.dashboard.DashboardLocationResponse
import com.future.tailormade.core.model.response.dashboard.DashboardTailorResponse
import com.future.tailormade.core.model.ui.dashboard.DashboardTailorUiModel

object DashboardMapper {

  fun mapToDashboardTailorUiModel(tailor: DashboardTailorResponse) = DashboardTailorUiModel(
      id = tailor.id,
      name = tailor.name,
      image = tailor.image,
      designs = tailor.designs,
      location = getLocation(tailor.location)
  )

  private fun getLocation(location: DashboardLocationResponse?) = location?.let { location ->
    (location.city.orEmpty() + location.country?.let {
      (", $it")
    }.orEmpty())
  }
}