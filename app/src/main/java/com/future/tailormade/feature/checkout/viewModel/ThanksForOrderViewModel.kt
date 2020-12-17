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
		private const val CART_UI_MODEL = "CART_UI_MODEL"
		private const val HISTORY_ID = "HISTORY_ID"
	}

	override fun getLogName() = "com.future.tailormade.feature.checkout.viewModel.ThanksForOrderViewModel"

	private var _cartUiModel: MutableLiveData<CartUiModel>
	val cartUiModel: LiveData<CartUiModel>
		get() = _cartUiModel

	private var _historyId: MutableLiveData<String>
	val historyId: LiveData<String>
		get() = _historyId

	init {
		_cartUiModel = savedStateHandle.getLiveData(CART_UI_MODEL, null)
		_historyId = savedStateHandle.getLiveData(HISTORY_ID, "")
	}

	fun setCartUiModel(cartUiModel: CartUiModel) {
		_cartUiModel.value = cartUiModel
		savedStateHandle.set(CART_UI_MODEL, _cartUiModel.value)
	}

	fun setHistoryId(id: String) {
		_historyId.value = id
		savedStateHandle.set(HISTORY_ID, _historyId.value)
	}
}