package com.future.tailormade.feature.checkout.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.ui.cart.CartUiModel

class ThanksForOrderViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    const val CART_UI_MODEL = "CART_UI_MODEL"
    const val HISTORY_ID = "HISTORY_ID"
  }

  override fun getLogName() = "com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel"

  private var _cartUiModel = MutableLiveData<CartUiModel>()
  val cartUiModel: LiveData<CartUiModel>
    get() = _cartUiModel

  private var _historyId = MutableLiveData<String>()

//  init {
//    _cartUiModel = savedStateHandle.getLiveData(CART_UI_MODEL)
//    _historyId = savedStateHandle.getLiveData(HISTORY_ID)
//  }

  fun setCartUiModel(cartUiModel: CartUiModel) {
    _cartUiModel.value = cartUiModel
  }

  fun setHistoryId(id: String) {
    _historyId.value = id
  }
}