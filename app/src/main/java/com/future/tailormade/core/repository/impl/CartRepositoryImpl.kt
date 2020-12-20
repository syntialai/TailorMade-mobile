package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.service.CartService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class CartRepositoryImpl @Inject constructor(private val cartService: CartService) :
    BaseRepository(), CartRepository {

  override fun getLogName(): String = "com.future.tailormade.core.repository.impl.CartRepositoryImpl"

  override suspend fun getCarts() = flow {
    emit(cartService.getCarts())
  }.flowOnIO()

  override suspend fun getCartById(id: String) = flow {
    emit(cartService.getCartById(id))
  }

  override suspend fun editCartItemQuantity(id: String,
      editQuantityRequest: CartEditQuantityRequest) = flow {
    emit(cartService.putEditCartItemQuantity(id, editQuantityRequest))
  }.flowOnIO()

  override suspend fun deleteCartItemById(id: String) = flow {
    emit(cartService.deleteCartItemById(id))
  }.flowOnIO()
}