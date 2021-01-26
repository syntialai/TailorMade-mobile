package com.future.tailormade.feature.cart.viewModel

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.future.tailormade.base.repository.AuthSharedPrefRepository
import com.future.tailormade.base.viewmodel.BaseViewModel
import com.future.tailormade.config.Constants
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.util.extension.onError
import com.future.tailormade.util.extension.orEmptyList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

class CartViewModel @ViewModelInject constructor(private val cartRepository: CartRepository,
    private val authSharedPrefRepository: AuthSharedPrefRepository,
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
      authSharedPrefRepository.userId?.let { userId ->
        cartRepository.getCarts(userId, page, itemPerPage).onError {
          setErrorMessage(Constants.FAILED_TO_GET_YOUR_CART_ITEM)
        }.collectLatest {
          addToList(it)
        }
      }
    }
  }

  @ExperimentalCoroutinesApi
  override fun fetchMore() {
    super.fetchMore()
    fetchCartData()
  }

  fun editCartItemQuantity(id: String, quantity: Int) {
    val request = CartEditQuantityRequest(quantity)
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        cartRepository.editCartItemQuantity(userId, id, request).onError {
          setErrorMessage(Constants.FAILED_TO_UPDATE_YOUR_CART_ITEM)
        }.collectLatest { response ->
          response.data?.let {
            setQuantity(it.id, it.quantity)
          }
        }
      }
    }
  }

  fun deleteCartItem(id: String) {
    launchViewModelScope {
      authSharedPrefRepository.userId?.let { userId ->
        cartRepository.deleteCartItemById(userId, id).onError {
          setErrorMessage(Constants.FAILED_TO_DELETE_CART_ITEM)
        }.collectLatest {
          deleteUiModelItem(id)
        }
      }
    }
  }

  private fun addToList(list: ArrayList<CartUiModel>) {
    val carts = arrayListOf<CartUiModel>()
    if (isFirstPage().not()) {
      carts.addAll(_cartUiModel.value.orEmptyList())
    }
    carts.addAll(list)
    _cartUiModel.value = carts
  }

  private fun deleteUiModelItem(id: String) {
    val cartItemIndex = getCartItemIndex(id)
    cartItemIndex?.let {
      _cartUiModel.value?.removeAt(it)
    }
  }

  private fun getCartItemIndex(id: String) = _cartUiModel.value?.indexOfFirst { it.id == id }

  private fun setQuantity(id: String, quantity: Int) {
    val cartItemIndex = getCartItemIndex(id)
    cartItemIndex?.let {
      _cartUiModel.value?.get(it)?.quantity = quantity
    }
  }
}