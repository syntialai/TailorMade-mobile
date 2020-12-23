package com.future.tailormade.core.service

import com.future.tailormade.base.model.response.BaseListResponse
import com.future.tailormade.base.model.response.BaseResponse
import com.future.tailormade.base.model.response.BaseSingleObjectResponse
import com.future.tailormade.core.api.AppApiUrl
import com.future.tailormade.core.model.request.cart.CartEditQuantityRequest
import com.future.tailormade.core.model.response.cart.CartEditQuantityResponse
import com.future.tailormade.core.model.response.cart.CartResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService {

  @GET(AppApiUrl.BASE_WISHLISTS_PATH)
  fun getCarts(): BaseListResponse<CartResponse>

  @GET(AppApiUrl.WISHLISTS_ID_PATH)
  fun getCartById(@Path("id") id: String): BaseSingleObjectResponse<CartResponse>

  @PUT(AppApiUrl.WISHLISTS_ID_EDIT_QUANTITY_PATH) fun putEditCartItemQuantity(
      @Path("id") id: String, @Body cartEditQuantityRequest: CartEditQuantityRequest):
      BaseSingleObjectResponse<CartEditQuantityResponse>

  @DELETE(AppApiUrl.WISHLISTS_ID_PATH)
  fun deleteCartItemById(@Path("id") id: String): BaseResponse
}