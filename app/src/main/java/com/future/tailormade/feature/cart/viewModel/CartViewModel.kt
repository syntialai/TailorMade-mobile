package com.future.tailormade.feature.cart.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.response.cart.CartDesignResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.model.ui.cart.CartDesignUiModel
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.toIndonesiaCurrencyFormat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class CartViewModel @ViewModelInject constructor(private val cartRepository: CartRepository,
    @Assisted private val savedStateHandle: SavedStateHandle) : BaseViewModel() {

  companion object {
    private const val CART_UI_MODEL = "CART"
  }

  override fun getLogName(): String = "com.future.tailormade.feature.cart.viewModel.CartViewModel"

  private var _cartUiModel = MutableLiveData<ArrayList<CartUiModel>>()
  val cartUiModel: LiveData<ArrayList<CartUiModel>>
    get() = _cartUiModel

  init {
    _cartUiModel = savedStateHandle.getLiveData(CART_UI_MODEL, arrayListOf())
  }

  @ExperimentalCoroutinesApi
  fun fetchCartData() {
    launchViewModelScope {
      cartRepository.getCarts().onStart {
        setStartLoading()
      }.onError {
        setFinishLoading()
        _errorMessage.value = "Failed to get your cart item. Please try again later."
      }.collectLatest { response ->
        response.data?.let {
          _cartUiModel.value = mapToCartUiModel(it)
          savedStateHandle.set(CART_UI_MODEL, _cartUiModel)
        }
        setFinishLoading()
      }
    }
  }

  fun editCartItemQuantity(id: String, quantity: Int) {
    val request = CartEditQuantityRequest(quantity)
    launchViewModelScope {
      cartRepository.editCartItemQuantity(id, request).onError {
        _errorMessage.value = "Failed to update your cart item. Please try again."
      }.collectLatest { response ->
        response.data?.let {
          setQuantity(it.id, it.quantity)
        }
      }
    }
  }

  fun deleteCartItem(id: String) {
    launchViewModelScope {
      cartRepository.deleteCartItemById(id).onError {
        _errorMessage.value = "Failed to delete your cart item. Please try again."
      }.collectLatest {
        deleteUiModelItem(id)
      }
    }
  }

  private fun deleteUiModelItem(id: String) {
    val cartItemIndex = getCartItemIndex(id)
    cartItemIndex?.let {
      _cartUiModel.value?.removeAt(it)
    }
  }

  private fun getCartItemIndex(id: String) = _cartUiModel.value?.indexOfFirst { it.id == id }

  private fun mapToCartUiModel(carts: List<CartResponse>): ArrayList<CartUiModel> {
    val cartUiModels = arrayListOf<CartUiModel>()
    carts.forEach { cart ->
      val cartUiModel = CartUiModel(cart.id, mapToCartDesignUiModel(cart.design), cart.quantity)
      cartUiModels.add(cartUiModel)
    }
    return cartUiModels
  }

  private fun mapToCartDesignUiModel(cartDesign: CartDesignResponse) = CartDesignUiModel(
      id = cartDesign.id,
      title = cartDesign.title,
      image = cartDesign.image,
      color = cartDesign.color,
      size = cartDesign.size,
      price = cartDesign.price.toIndonesiaCurrencyFormat(),
      discount = setDiscount(cartDesign.price, cartDesign.discount)
  )

  private fun setDiscount(price: Double, discount: Double) = if (discount == 0.0) {
    null
  } else {
    (price - discount).toIndonesiaCurrencyFormat()
  }

  private fun setQuantity(id: String, quantity: Int) {
    val cartItemIndex = getCartItemIndex(id)
    cartItemIndex?.let {
      _cartUiModel.value?.get(it)?.quantity = quantity
    }
  }
}