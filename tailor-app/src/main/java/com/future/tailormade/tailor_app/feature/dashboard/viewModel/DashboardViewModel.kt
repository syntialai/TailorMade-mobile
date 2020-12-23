package com.future.tailormade.tailor_app.feature.dashboard.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.tailor_app.core.model.ui.DashboardDesignUiModel

class DashboardViewModel : BaseViewModel() {

  override fun getLogName() =
      "com.future.tailormade.feature.dashboard.viewModel.DashboardViewModel.${Constants.TAILOR}"

  private var _designs = MutableLiveData<DashboardDesignUiModel>()
}