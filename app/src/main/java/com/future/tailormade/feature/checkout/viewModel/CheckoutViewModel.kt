package com.future.tailormade.feature.checkout.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.response.cart.CartDesignResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.model.response.cart.CartSizeDetailResponse
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.util.extension.flowOnMainWithLoadingDialog
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
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

	private var _id = MutableLiveData<String>()
	val id: LiveData<String>
		get() = _id

	private var _cartResponse = MutableLiveData<CartResponse>()

	private var _cartUiModel = MutableLiveData<CartUiModel>()
	val cartUiModel: LiveData<CartUiModel>
		get() = _cartUiModel

	private var _measurementValues = MutableLiveData<MutableList<String>>()
	val measurementValues: LiveData<MutableList<String>>
		get() = _measurementValues

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
					_cartUiModel.value = mapToCartUiModel(it)
					_measurementValues.value = it.design.sizeDetail?.let { sizeDetail ->
						mapToMeasurementDetail(sizeDetail)
					}
					savedStateHandle.set(CART_RESPONSE, it)
					setFinishLoading()
				}
			}
		}
	}

	fun setCheckoutMeasurementDetail(values: List<String>) {
		_measurementValues.value = values.toMutableList()
	}

	private fun getTotal(price: Double, quantity: Int) = (price * quantity).toIndonesiaCurrencyFormat()

	private fun mapToCartUiModel(cartResponse: CartResponse) = CartUiModel(
			id = cartResponse.id,
			design = mapToCartDesignUiModel(cartResponse.design),
			quantity = cartResponse.quantity,
			totalPrice = getTotal(cartResponse.design.price, cartResponse.quantity),
			totalDiscount = getTotal(cartResponse.design.discount, cartResponse.quantity),
			totalPayment = getTotal(
					cartResponse.design.price - cartResponse.design.discount, cartResponse.quantity)
	)

	private fun mapToCartDesignUiModel(design: CartDesignResponse) = CartDesignUiModel(
			id = design.id,
			color = design.color,
			discount = (design.price - design.discount).toIndonesiaCurrencyFormat(),
			image = design.image,
			price = design.price.toIndonesiaCurrencyFormat(),
			size = design.size,
			title = design.title,
	)

	private fun mapToMeasurementDetail(sizeDetail: CartSizeDetailResponse) = mutableListOf(
			sizeDetail.chest.toString(),
			sizeDetail.waist.toString(),
			sizeDetail.hips.toString(),
			sizeDetail.neckToWaist.toString(),
			sizeDetail.inseam.toString()
	)
}