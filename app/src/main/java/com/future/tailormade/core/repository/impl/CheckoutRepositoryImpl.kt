package com.future.tailormade.core.repository.impl

import com.future.tailormade.base.repository.BaseRepository
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.repository.CheckoutRepository
import com.future.tailormade.core.service.CheckoutService
import com.future.tailormade.util.extension.flowOnIO
import javax.inject.Inject
import kotlinx.coroutines.flow.flow

class CheckoutRepositoryImpl @Inject constructor(private val checkoutService: CheckoutService) :
    BaseRepository(), CheckoutRepository {

  override fun getLogName() = "com.future.tailormade.core.repository.impl.CheckoutRepositoryImpl"

  override suspend fun checkoutCartItem(id: String, checkoutRequest: CheckoutRequest) = flow {
    emit(checkoutService.postCheckoutCartItem(id, checkoutRequest))
  }.flowOnIO()
}