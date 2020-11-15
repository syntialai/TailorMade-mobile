package com.future.tailormade.base.view

sealed class ViewState {

  data class Loading(var isLoading: Boolean) : ViewState()

  data class Error(var message: String? = null) : ViewState()

  object Unauthorized : ViewState()

  object NoInternetConnection : ViewState()
}