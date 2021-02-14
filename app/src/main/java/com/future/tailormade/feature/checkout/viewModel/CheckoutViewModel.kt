package com.future.tailormade.feature.checkout.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.mapper.CartMapper
import com.future.tailormade.core.model.request.checkout.CheckoutMeasurementRequest
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.util.extension.onError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class CheckoutViewModel @ViewModelInject constructor(private val cartRepository: CartRepository,
    private val checkoutRepository: CheckoutRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val CART_UI_MODEL = "CART_UI_MODEL"
    private const val ID = "ID"
  }

  override fun getLogName() = "com.future.tailormade.feature.checkout.viewModel.CheckoutViewModel"

  private var _id = MutableLiveData<String>()
  val id: LiveData<String>
    get() = _id

  private var _cartUiModel = MutableLiveData<CartUiModel>()
  val cartUiModel: LiveData<CartUiModel>
    get() = _cartUiModel

  private var _measurementValues = MutableLiveData<MutableList<String>>()
  val measurementValues: LiveData<MutableList<String>>
    get() = _measurementValues

  private var _historyId = MutableLiveData<String>()
  val historyId: LiveData<String>
    get() = _historyId

//  init {
//    _id = savedStateHandle.getLiveData(ID, "")
//    _cartUiModel = savedStateHandle.getLiveData(CART_UI_MODEL)
//    _measurementValues.value = _cartUiModel.value?.design?.sizeDetail?.let { sizeDetail ->
//      CartMapper.mapToMeasurementDetail(sizeDetail)
//    }
//  }

  @ExperimentalCoroutinesApi
  fun checkoutItem(id: String, specialInstructions: String) {
    authSharedPrefRepository.userId?.let { userId ->
      launchViewModelScope {
        val checkoutRequest = getCheckoutRequest(specialInstructions)
        checkoutRepository.checkoutCartItem(userId, id, checkoutRequest).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_CHECKOUT_ITEM)
          appLogger.logOnError(it.message.toString(), it)
        }.collectLatest {
          setFinishLoading()
          _historyId.value = it.id
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  fun getCartItemById(id: String) {
    authSharedPrefRepository.userId?.let { userId ->
      launchViewModelScope {
        cartRepository.getCartById(userId, id).onStart {
          setStartLoading()
        }.onError {
          setFinishLoading()
          setErrorMessage(Constants.FAILED_TO_GET_CHECKOUT_DATA)
        }.collectLatest { cartUiModel ->
          _cartUiModel.value = cartUiModel
          _measurementValues.value = cartUiModel.design.sizeDetail?.let { sizeDetail ->
            CartMapper.mapToMeasurementDetail(sizeDetail)
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

  private fun getCheckoutRequest(specialInstructions: String) = CheckoutRequest(
      measurements = getCheckoutMeasurementRequest(), specialInstructions = specialInstructions)

  private fun getCheckoutMeasurementRequest(): CheckoutMeasurementRequest {
    val measurements = arrayListOf<Float>()
    _measurementValues.value?.forEach {
      measurements.add(it.toFloat())
    }
    return CheckoutMeasurementRequest(measurements[0], measurements[1], measurements[2],
        measurements[3], measurements[4])
  }
}