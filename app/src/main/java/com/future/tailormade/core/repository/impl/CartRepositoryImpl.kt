package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.mapper.CartMapper
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.service.CartService
import com.future.tailormade.util.extension.flowOnIO
import com.future.tailormade_design_detail.core.model.response.AddToCartResponse
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CartRepositoryImpl @Inject constructor(private val cartService: CartService,
    private val ioDispatcher: CoroutineDispatcher) : BaseRepository(), CartRepository {

  override fun getLogName(): String = "com.future.tailormade.core.repository.impl.CartRepositoryImpl"

  override suspend fun getCarts(userId: String, page: Int, itemPerPage: Int) = flow {
    cartService.getCarts(userId, page, itemPerPage).data?.let {
      emit(CartMapper.mapToCartUiModel(it))
    }
  }.flowOn(ioDispatcher)

  override suspend fun getCartById(userId: String, id: String) = flow {
    cartService.getCartById(userId, id).data?.let {
      emit(CartMapper.mapToCartUiModel(it))
    }
  }.flowOn(ioDispatcher)

  override suspend fun editCartItemQuantity(userId: String, id: String,
      editQuantityRequest: CartEditQuantityRequest) = flow {
    cartService.putEditCartItemQuantity(userId, id, editQuantityRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)

  override suspend fun deleteCartItemById(userId: String, id: String) = flow {
    cartService.deleteCartItemById(userId, id).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)
}