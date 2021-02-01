package com.future.tailormade.core.model.ui.dashboard

import com.future.tailormade.core.model.response.dashboard.DashboardDesignResponse

data class DashboardTailorUiModel(

    var id: String,

    var name: String,

    var image: String? = null,

    var location: String? = null,

    var designs: List<DashboardDesignResponse>? = null)