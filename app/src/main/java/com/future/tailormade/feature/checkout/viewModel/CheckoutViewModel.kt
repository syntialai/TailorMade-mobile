package com.future.tailormade.feature.checkout.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.util.extension.flowOnMainWithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade_auth.core.repository.impl.AuthSharedPrefRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class CheckoutViewModel @ViewModelInject constructor(private val cartRepository: CartRepository,
		private val checkoutRepository: CheckoutRepository,
		private val authSharedPrefRepository: AuthSharedPrefRepository,
		@Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

	companion object {
		private const val CART_RESPONSE = "CART_RESPONSE"
	}

	override fun getLogName() = "com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel"

	private var _cartResponse = MutableLiveData<CartResponse>()
	val cartResponse: LiveData<CartResponse>
		get() = _cartResponse

	init {
		_cartResponse = savedStateHandle.getLiveData(CART_RESPONSE)
	}

	@ExperimentalCoroutinesApi
	fun getCartItemById(id: String) {
		launchViewModelScope {
			cartRepository.getCartById(id).flowOnMainWithLoadingDialog(this).onStart {
				setStartLoading()
			}.onError {
				setFinishLoading()
				setErrorMessage("Failed to get data. Please try again later.")
			}.collectLatest { response ->
				response.data?.let {
					_cartResponse.value = it
					savedStateHandle.set(CART_RESPONSE, it)
					setFinishLoading()
				}
			}
		}
	}
}