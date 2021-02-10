package com.future.tailormade.core.repository

import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.response.cart.CartEditQuantityResponse
import com.future.tailormade.core.model.ui.cart.CartUiModel
import com.future.tailormade_design_detail.core.model.response.AddToCartResponse
import kotlinx.coroutines.flow.Flow

interface CartRepository {

  suspend fun getCarts(userId: String, page: Int, itemPerPage: Int): Flow<ArrayList<CartUiModel>>

  suspend fun getCartById(userId: String, id: String): Flow<CartUiModel>

  suspend fun editCartItemQuantity(userId: String, id: String,
      editQuantityRequest: CartEditQuantityRequest): Flow<CartEditQuantityResponse>

  suspend fun deleteCartItemById(userId: String, id: String): Flow<AddToCartResponse>
}