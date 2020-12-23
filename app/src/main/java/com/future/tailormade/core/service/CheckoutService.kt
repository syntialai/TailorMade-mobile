package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.request.checkout.CheckoutRequest
import com.future.tailormade.core.model.response.checkout.CheckoutResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface CheckoutService {

  @POST(AppApiUrl.WISHLISTS_ID_CHECKOUT_PATH)
  fun postCheckoutCartItem(@Path("id") id: String,
      @Body checkoutRequest: CheckoutRequest): BaseSingleObjectResponse<CheckoutResponse>
}