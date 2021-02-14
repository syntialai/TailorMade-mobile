package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.core.service.CheckoutService
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CheckoutRepositoryImpl @Inject constructor(private val checkoutService: CheckoutService,
    private val ioDispatcher: CoroutineDispatcher) : BaseRepository(), CheckoutRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.CheckoutRepositoryImpl"

  override suspend fun checkoutCartItem(userId: String, id: String,
      checkoutRequest: CheckoutRequest) = flow {
    checkoutService.postCheckoutCartItem(userId, id, checkoutRequest).data?.let {
      emit(it)
    }
  }.flowOn(ioDispatcher)
}