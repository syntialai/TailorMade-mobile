package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutService {

  @POST(AppApiUrl.USERS_ID_WISHLISTS_ID_CHECKOUT_PATH)
  suspend fun postCheckoutCartItem(@Path("userId") userId: String, @Path("id") id: String,
      @Body checkoutRequest: CheckoutRequest): BaseSingleObjectResponse<CheckoutResponse>
}