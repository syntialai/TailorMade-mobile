package com.future.tailormade.feature.checkout.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.request.checkout.CheckoutDesignRequest
import com.future.tailormade.core.model.request.checkout.CheckoutMeasurementRequest
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
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
    private const val ID = "ID"
  }

  override fun getLogName() = "com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel"

  private var _id = MutableLiveData<String>()
  val id: LiveData<String>
    get() = _id

  private var _cartResponse: MutableLiveData<CartResponse>

  private var _cartUiModel = MutableLiveData<CartUiModel>()
  val cartUiModel: LiveData<CartUiModel>
    get() = _cartUiModel

  private var _measurementValues = MutableLiveData<MutableList<String>>()
  val measurementValues: LiveData<MutableList<String>>
    get() = _measurementValues

  private var _historyId = MutableLiveData<String>()
  val historyId: LiveData<String>
    get() = _historyId

  init {
    _id = savedStateHandle.getLiveData(ID, "")
    _cartResponse = savedStateHandle.getLiveData(CART_RESPONSE)
    _cartUiModel.value = _cartResponse.value?.let { mapToCartUiModel(it) }
    _measurementValues.value = _cartResponse.value?.design?.sizeDetail?.let { sizeDetail ->
      mapToMeasurementDetail(sizeDetail)
    }
  }

  @ExperimentalCoroutinesApi
  fun checkoutItem(id: String) {
    launchViewModelScope {
      getCheckoutRequest()?.let { request ->
        checkoutRepository.checkoutCartItem(id, request).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage("Failed to checkout item, please try again.")
        }.collectLatest { response ->
          response.data?.let {
            setFinishLoading()
            _historyId.value = it.id
          }
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  fun getCartItemById(id: String) {
    launchViewModelScope {
      cartRepository.getCartById(id).flowOnMainWithLoadingDialog(this).onStart {
        setStartLoading()
      }.onError {
        setFinishLoading()
        setErrorMessage(Constants.generateFailedFetchError("cart"))
      }.collectLatest { response ->
        response.data?.let {
          _cartResponse.value = it
          _cartUiModel.value = mapToCartUiModel(it)
          _measurementValues.value = it.design.sizeDetail?.let { sizeDetail ->
            mapToMeasurementDetail(sizeDetail)
          }
          setFinishLoading()
        }
      }
    }
  }

  fun setCheckoutMeasurementDetail(values: List<String>) {
    _measurementValues.value = values.toMutableList()
  }

  fun setId(id: String) {
    _id.value = id
  }

  private fun getCheckoutRequest() = _cartResponse.value?.let { cartResponse ->
    CheckoutRequest(
        design = getCheckoutDesignRequest(cartResponse),
        measurement = getCheckoutMeasurementRequest(),
        specialInstructions = ""
    )
  }

  private fun getCheckoutDesignRequest(cartResponse: CartResponse) = CheckoutDesignRequest(
      id = cartResponse.design.id,
      color = cartResponse.design.color,
      discount = cartResponse.design.discount,
      image = cartResponse.design.image,
      price = cartResponse.design.price,
      size = cartResponse.design.size,
      title = cartResponse.design.title,
      tailorId = cartResponse.tailorId,
      tailorName = cartResponse.tailorName.orEmpty()
  )

  private fun getCheckoutMeasurementRequest(): CheckoutMeasurementRequest {
    val measurements = arrayListOf<Double>()
    _measurementValues.value?.forEach {
      measurements.add(it.toDouble())
    }
    return CheckoutMeasurementRequest(measurements[0], measurements[1], measurements[2],
        measurements[3], measurements[4])
  }

  private fun getTotal(price: Double, quantity: Int) = price * quantity

  private fun getTotalText(price: Double, quantity: Int) =
      getTotal(price, quantity).toIndonesiaCurrencyFormat()

  private fun mapToCartUiModel(cartResponse: CartResponse) = CartUiModel(
      id = cartResponse.id,
      design = mapToCartDesignUiModel(cartResponse.design),
      quantity = cartResponse.quantity,
      totalPrice = getTotalText(cartResponse.design.price, cartResponse.quantity),
      totalDiscount = getTotalText(cartResponse.design.discount, cartResponse.quantity),
      totalPayment = getTotalText(
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