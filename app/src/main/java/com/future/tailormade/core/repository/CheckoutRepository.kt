package com.future.tailormade.core.repository

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import kotlinx.coroutines.flow.Flow

interface CheckoutRepository {

  suspend fun checkoutCartItem(id: String, checkoutRequest: CheckoutRequest):
      Flow<BaseSingleObjectResponse<CheckoutResponse>>
}