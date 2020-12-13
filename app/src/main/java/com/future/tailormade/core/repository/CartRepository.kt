package com.future.tailormade.core.repository

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import kotlinx.coroutines.flow.Flow

interface CartRepository {

	suspend fun getCarts(): Flow<BaseListResponse<CartResponse>>

	suspend fun getCartById(id: String): Flow<BaseSingleObjectResponse<CartResponse>>
}