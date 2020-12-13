package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.model.response.cart.CartResponse
import com.future.tailormade.core.repository.CartRepository
import com.future.tailormade.core.service.CartService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CartRepositoryImpl @Inject constructor(private val cartService: CartService) :
		BaseRepository(), CartRepository {

	override fun getLogName(): String = "com.future.tailormade.core.repository.impl.CartRepositoryImpl"

	override suspend fun getCarts() = flow {
		emit(cartService.getCarts())
	}

	override suspend fun getCartById(id: String) = flow {
		emit(cartService.getCartById(id))
	}
}