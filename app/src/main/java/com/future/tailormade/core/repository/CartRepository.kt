package com.future.tailormade.core.repository

import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.response.cart.CartEditQuantityResponse
import com.future.tailormade.core.model.ui.cart.CartUiModel
import kotlinx.coroutines.flow.Flow

interface CartRepository {

  suspend fun getCarts(userId: String, page: Int, itemPerPage: Int): Flow<ArrayList<CartUiModel>>

  suspend fun getCartById(userId: String, id: String): Flow<CartUiModel>

  suspend fun editCartItemQuantity(
      userId: String, id: String, editQuantityRequest: CartEditQuantityRequest):
      Flow<BaseSingleObjectResponse<CartEditQuantityResponse>>

  suspend fun deleteCartItemById(userId: String, id: String): Flow<BaseResponse>
}