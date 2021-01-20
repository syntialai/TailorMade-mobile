package com.future.tailormade.core.repository

import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {

  suspend fun checkoutCartItem(userId: String, id: String, checkoutRequest: CheckoutRequest):
      Flow<CheckoutResponse?>
}